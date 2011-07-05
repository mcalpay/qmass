package org.mca.qmass.console;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.mca.qmass.console.el.QMassELContext;
import org.mca.qmass.core.QMass;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * User: malpay
 * Date: 05.Tem.2011
 * Time: 10:14:50
 */
public class ELConsoleMain {

    private static final Log logger = LogFactory.getLog(SimpleConsoleMain.class);

    private static QMassConsoleAppender appender;

    public static void main(String... args) throws Exception {
        appender = (QMassConsoleAppender)
                Logger.getRootLogger().getAppender("QCONSOLE");

        QMass qmass = QMass.getQMass();
        ExpressionFactory expressionFactory = ExpressionFactory.newInstance();
        ELContext elContext = new QMassELContext(qmass);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        final boolean[] runing = {true};
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                runing[0] = false;
            }
        });

        appender.print();

        while (runing[0]) {
            System.out.print("> ");
            String untrimmedLine = bufferedReader.readLine();
            String line = (untrimmedLine != null) ? untrimmedLine.trim() : "";
            try {
                if ("bye".equals(line)) {
                    runing[0] = false;
                    println("bye bye");
                } else if ("".equals(line)) {
                    println("");
                } else {
                    ValueExpression valueExpression =
                            expressionFactory.createValueExpression(elContext, "${" + line + "}",
                                    Object.class);
                    println("returns : " + valueExpression.getValue(elContext));
                }
            } catch (Exception e) {
                logger.debug("Console error", e);
                println("command failed '" + line + "'");
            }
        }

        qmass.end();
    }

    private static void println(String text) {
        System.out.println("[QMassConsole] Start system logs;");
        appender.print();
        System.out.println("[QMassConsole] End system logs;");
        if (!text.isEmpty()) {
            System.out.println("[QMassConsole] " + text + "\n");
        }
    }

}
