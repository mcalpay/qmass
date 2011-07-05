package org.mca.qmass.console.el;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mca.qmass.core.QMass;
import org.mca.qmass.grid.QMassGrid;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

/**
 * User: malpay
 * Date: 04.Tem.2011
 * Time: 15:10:19
 */
public class EFMain {
    
    private static final Log logger = LogFactory.getLog(EFMain.class);

    public static void main(String... args) throws Exception {
        logger.debug("go");
        Long test = 1L;
        QMass qmass = QMass.getQMass();
        String var = "car";
        ExpressionFactory expressionFactory = ExpressionFactory.newInstance();
        ELContext elContext = new QMassELContext(qmass);
        String gridid = qmass.getId() + "/Grid/" + var;
        QMassGrid grid = (QMassGrid) qmass.getService(
                gridid);
        if (grid == null) {
            grid = new QMassGrid(var, qmass);
        }

        //grid.put("first", "foo");
        ValueExpression putExp = expressionFactory.createValueExpression(elContext, "${car.put('first','boo')}",
                Object.class);
        System.out.println("${car.put('first','foo')} : " + putExp.getValue(elContext));
        ValueExpression getExp = expressionFactory.createValueExpression(elContext, "${car.get('first')}",
                Object.class);
        System.out.println("${car.get('first')} : " + getExp.getValue(elContext));
        ValueExpression remExp = expressionFactory.createValueExpression(elContext, "${car.remove('first')}",
                Object.class);
        System.out.println("${car.remove('first')} : " + remExp.getValue(elContext));
        System.out.println("${car.get('first')} : " + getExp.getValue(elContext));

        QMass.getQMass().end();
    }

}
