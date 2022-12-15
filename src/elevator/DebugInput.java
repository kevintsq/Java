package elevator;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DebugInput implements Runnable {
    private final OutputStream outputStream;

    public DebugInput(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        Timer timer = new Timer(); // 初始化一个定时器
        Pattern pattern = Pattern.compile("\\[(?<time>.+)](?<cmd>.+)");
        long currentMillis = System.currentTimeMillis();
        long maxMillis = 0; // 记录最后一条输入的时间
        while (scanner.hasNext()) {
            String input = scanner.nextLine(); // 读取一行输入
            Matcher matcher = pattern.matcher(input);
            matcher.find();
            long millis = (long) (Double.parseDouble(matcher.group("time")) * 1000);
            maxMillis = millis; // 更新maxMillis
            timer.schedule(new TimerTask() { // 创建定时任务
                @Override
                public void run() {
                    try {
                        outputStream.write(matcher.group("cmd").getBytes());
                        outputStream.write('\n');
                        outputStream.flush(); // 强制刷新
                    } catch (IOException ignored) {
                        // ignore this
                    }
                }
            }, new Date(currentMillis + millis));
        }
        // 关闭管道流和定时器，添加最后一个定时任务
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    outputStream.close();
                } catch (IOException ignored) {
                    // ignore this
                }
                timer.cancel(); // 关闭定时器，不加这句则定时器可能无限等待
            }
        }, new Date(currentMillis + maxMillis + 100));
    }
}
