package org.mca.qmass.console.el;

import org.mca.qmass.grid.QMassGrid;

import javax.el.BeanELResolver;
import javax.el.ELContext;
import java.io.Serializable;

/**
 * User: malpay
 * Date: 04.Tem.2011
 * Time: 16:36:14
 */
public class QMassELResolver extends BeanELResolver {

    @Override
    public Object getValue(ELContext context, Object base, Object property) {
        if(base instanceof QMassGrid) {
            QMassGrid grid = (QMassGrid) base;
            context.setPropertyResolved(true);
            Serializable serializable = grid.get((Serializable) property);
            return serializable;
        }
        return super.getValue(context, base, property);
    }
}
