package Actions;

import Services.GoogleService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class TranslateAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        Editor editor = anActionEvent.getData(PlatformDataKeys.EDITOR);
        Project project = anActionEvent.getRequiredData(PlatformDataKeys.PROJECT);
        if (editor != null) {
            String selectedText = editor.getSelectionModel().getSelectedText();
            if (selectedText != null && !selectedText.isEmpty()) {
                String encodedText = URLEncoder.encode(selectedText, StandardCharsets.UTF_8);
                try {
                    GoogleService service = new GoogleService();
                    Document document = editor.getDocument();
                    Caret primaryCaret = editor.getCaretModel().getPrimaryCaret();
                    int start = primaryCaret.getSelectionStart();
                    int end = primaryCaret.getSelectionEnd();
                    WriteCommandAction.runWriteCommandAction(project, () ->
                            document.replaceString(start, end, service.translate("en", "ru", encodedText))
                    );
                    primaryCaret.removeSelection();
                } catch (Exception ignored) {
                }
            }
        }
    }
}
