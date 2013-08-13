package fr.aumgn.cwj.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class OneLinerFormatter extends Formatter {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("[dd/MM-hh:mm:ss]");

    @Override
    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder();
        builder.append(DATE_FORMAT.format(new Date(record.getMillis())));
        builder.append(" ");
        builder.append(record.getLevel().getName().toUpperCase());
        builder.append(": ");
        builder.append(formatMessage(record));
        builder.append(" (@");
        builder.append(record.getSourceClassName());
        builder.append("#");
        builder.append(record.getSourceMethodName());
        builder.append(")\n");

        Throwable thrown = record.getThrown();
        if (thrown != null) {
            try {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                thrown.printStackTrace(pw);
                pw.close();
                builder.append(sw.toString());
            }
            catch (Exception ex) {
            }
        }

        return builder.toString();
    }
}
