import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class TestAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        Editor editor = anActionEvent.getData(PlatformDataKeys.EDITOR);
        if (editor != null) {
            String selectedText = editor.getSelectionModel().getSelectedText();
            if (selectedText != null && !selectedText.isEmpty()) {
//                Messages.showMessageDialog(editor.getSelectionModel().getSelectedText(), "Test", Messages.getInformationIcon());
                String encodedText = URLEncoder.encode(selectedText, StandardCharsets.UTF_8);
                try {
                    URL url = new URL(String.format("https://translate.googleapis.com/translate_a/single?client=gtx&sl=en&tl=ru&hl=ru&dt=t&dt=bd&dj=1&source=icon&tk=810526.810526&q=%s", encodedText));
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestProperty("Accept-Charset", "UTF-8");
                    connection.setRequestMethod("GET");
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuffer response = new StringBuffer();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();
                        JSONObject json = new JSONObject(response.toString());
                        if (!json.getJSONArray("sentences").isEmpty()) {
                            JSONObject sentence = (JSONObject) json.getJSONArray("sentences").get(0);
                            String translate = new String(sentence.getString("trans").getBytes(), StandardCharsets.UTF_8);

                            Project project = anActionEvent.getRequiredData(PlatformDataKeys.PROJECT);
                            Document document = editor.getDocument();
                            Caret primaryCaret = editor.getCaretModel().getPrimaryCaret();
                            int start = primaryCaret.getSelectionStart();
                            int end = primaryCaret.getSelectionEnd();
                            WriteCommandAction.runWriteCommandAction(project, () ->
                                    document.replaceString(start, end, translate)
                            );
                            primaryCaret.removeSelection();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
