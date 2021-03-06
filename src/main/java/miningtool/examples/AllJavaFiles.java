package miningtool.examples;

import miningtool.common.*;
import miningtool.parse.java.GumTreeJavaParser;
import miningtool.paths.PathMiner;
import miningtool.paths.PathRetrievalSettings;
import miningtool.paths.storage.VocabularyPathStorage;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.stream.Collectors;

//Retrieve paths from Java files, using a GumTree parser.
public class AllJavaFiles {
    private static final String INPUT_FOLDER = "./testData";
    private static final String OUTPUT_FOLDER = "out_examples/allJavaFiles_GumTree_java";

    public static void runExample() {
        final PathMiner miner = new PathMiner(new PathRetrievalSettings(5,5));
        final PathStorage pathStorage = new VocabularyPathStorage();

        final Path inputFolder = Paths.get(INPUT_FOLDER);

        FileVisitor<Path> fileVisitor = new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
                Node fileTree = new GumTreeJavaParser().parse(new FileInputStream(file.toFile()));
                if (fileTree == null) {
                    return FileVisitResult.CONTINUE;
                }
                final Collection<ASTPath> paths = miner.retrievePaths(fileTree);
                final Collection<PathContext> pathContexts = paths
                        .stream().map(PathUtilKt::toPathContext).collect(Collectors.toList());

                pathStorage.store(pathContexts, file.toAbsolutePath().toString());

                return FileVisitResult.CONTINUE;
            }
        };

        try {
            Files.walkFileTree(inputFolder, fileVisitor);
            pathStorage.save(OUTPUT_FOLDER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
