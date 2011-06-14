package org.mca.qmass.grid;

import org.mca.qmass.grid.request.Request;
import org.mca.qmass.grid.request.Response;

import java.io.Serializable;

/**
 * User: malpay
 * Date: 13.Haz.2011
 * Time: 16:20:38
 */
public interface RequestResponseHandler {

    RequestResponseHandler startWork();

    Serializable sendPutRequest(Serializable key, Serializable value);

    Serializable sendGetRequest(Serializable key);

    Response consumeResponse(Serializable no);

    RequestResponseHandler endWork();
}
