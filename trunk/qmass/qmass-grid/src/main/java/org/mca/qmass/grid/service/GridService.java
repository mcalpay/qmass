package org.mca.qmass.grid.service;

import org.mca.qmass.core.Service;
import org.mca.qmass.grid.event.GetRequestEvent;
import org.mca.qmass.grid.event.MergeRequestEvent;
import org.mca.qmass.grid.event.PutRequestEvent;
import org.mca.qmass.grid.event.RemoveRequestEvent;
import org.mca.qmass.grid.request.Response;

import java.io.Serializable;

/**
 * User: malpay
 * Date: 15.Haz.2011
 * Time: 10:03:26
 */
public interface GridService extends Service {

    Serializable sendPut(Serializable key, Serializable value);

    Serializable sendMerge(Serializable key, Serializable value);

    Serializable sendGet(Serializable key);

    Serializable sendRemove(Serializable key);

    void saveResponse(Response response);

    void respondToPut(PutRequestEvent event);

    void respondToGet(GetRequestEvent event);

    void respondToRemove(RemoveRequestEvent removeRequestEvent);

    void respondToMerge(MergeRequestEvent event);

    Response consumeResponse(Serializable no);

}
