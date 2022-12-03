package www.larkmidtable.com.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import www.larkmidtable.com.util.DateUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;


public class HuLogger {
    private static Logger logger = LoggerFactory.getLogger("HuLogger logger");



    /**
     * append log
     *
     * @param call
     * @param appendLog
     */
    private static void logDetail(StackTraceElement call, String appendLog) {

        // "yyyy-MM-dd HH:mm:ss [fileName.MethodName-LineNumber] log";
        StringBuffer buffer = new StringBuffer();
        buffer.append(DateUtil.formatDateTime(new Date())).append(" ")
                .append("[" + call.getFileName().replace("java", "") + call.getMethodName())
                .append("-" + call.getLineNumber() + "]").append(" ")
                .append(appendLog != null ? appendLog : "");
        String formatAppendLog = buffer.toString();
        if(logger.isDebugEnabled()){
            logger.debug(formatAppendLog);
        }
        String logFileName = HuFileAppender.contextHolder.get();
        if (logFileName != null && logFileName.trim().length() > 0) {
            HuFileAppender.appendLog(logFileName, formatAppendLog);
        } else {
            logger.info(">>> {}", formatAppendLog);
        }
    }

    /**
     * append log with pattern
     *
     * @param appendLogPattern   like "aaa {} bbb {} ccc"
     * @param appendLogArguments like "111, true"
     */
    public static void log(String appendLogPattern, Object... appendLogArguments) {
        FormattingTuple ft = MessageFormatter.arrayFormat(appendLogPattern, appendLogArguments);
        String appendLog = ft.getMessage();
        /*appendLog = appendLogPattern;
        if (appendLogArguments!=null && appendLogArguments.length>0) {
            appendLog = MessageFormat.format(appendLogPattern, appendLogArguments);
        }*/

        StackTraceElement callInfo = new Throwable().getStackTrace()[1];
        logDetail(callInfo, appendLog);
    }

    /**
     * append exception stack
     *
     * @param e
     */
    public static void error(Throwable e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        String appendLog = stringWriter.toString();
        StackTraceElement callInfo = new Throwable().getStackTrace()[1];
        logDetail(callInfo, appendLog);
    }

}
