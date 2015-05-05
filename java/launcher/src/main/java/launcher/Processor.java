package launcher;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class Processor {

    public static Process execute(String command, File directory) throws IOException {
        if (command == null)
            throw new NullPointerException("command must not be null");

        ProcessBuilder builder = new ProcessBuilder();
        builder.redirectErrorStream(true);
        builder.command(command.trim().split(" "));
        builder.directory(directory);

        Process process = builder.start();
        process.getOutputStream().close();
        plugTogether(System.out, process.getInputStream());
        return process;
    }

    private static void plugTogether(final InputStream in, final OutputStream out) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                BufferedInputStream bin = new BufferedInputStream(in);
                try {
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = bin.read(buf)) != -1)
                        out.write(buf, 0, len);
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    private static void plugTogether(OutputStream out, InputStream in) {
        plugTogether(in, out);
    }

}
