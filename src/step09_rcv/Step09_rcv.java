package step09_rcv;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Step09_rcv implements MessageListener {

    private final Log log = LogFactory.getLog(step08_rcv.Step08_rcv.class);

    public void onMessage(Message message) {
        log.debug("==== onMessage Start ====");
        String textMsg = "";
        if (message instanceof TextMessage) {
            try {
                textMsg = ((TextMessage) message).getText();
                log.debug("received Message : " + textMsg);



                // 저장 경로와 파일 이름 지정
                String directory = "C:/edu/file"; // 저장할 디렉터리 경로
                String fileName = "202501072.txt"; // 저장할 파일 이름
                String content = textMsg;

                try {
                    // 경로 생성 (디렉터리가 없으면 생성)
                    Path path = Paths.get(directory);
                    if (!Files.exists(path)) {
                        Files.createDirectories(path);
                        System.out.println("Directory created: " + path);
                    }

                    // 파일 저장
                    String filePath = directory + "/" + fileName;
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                        writer.write(content);
                        writer.newLine();
                        System.out.println("File saved successfully at: " + filePath);
                    }

                } catch (IOException e) {
                    System.out.println("An error occurred while writing the file.");
                    e.printStackTrace();
                }

            } catch (JMSException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("Message must be of type TestMessage");
        }
        log.debug("==== onMessage End ====");

    }

}