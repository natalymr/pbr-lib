public class Main {
    @NotNull
    @Override
    public String preprocessOnPaste(Project project, PsiFile file, Editor editor, String text, RawText rawText) {
        if (isApplicable(file) && isMvnDependency(text)) {
            GradleActionsUsagesCollector.trigger(project, "PasteMvnDependency");
            return toGradleDependency(text);
        }
        return text;
    }

    @NotNull
    @Override
    public String preprocessOnPaste(Project project, PsiFile file, Editor editor, String text, RawText rawText) {
        if (isApplicable(file) && isMvnDependency(text)) {
            return toGradleDependency(text);
        }
        return text;
    }


    public static void main(String[] args) {
        System.out.println("Hello world");
    }
}