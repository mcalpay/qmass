package org.mca.qmass.grid;

import org.mca.qmass.grid.request.Request;

import java.io.Serializable;

/**
 * User: malpay
 * Date: 13.Haz.2011
 * Time: 16:20:38
 */
public interface RequestResponseHandler {

    RequestResponseHandler startWork();

    Integer sendPutRequest(Serializable key, Serializable value);

    Integer sendGetRequest(Serializable key);

    Request response(int no);

    RequestResponseHandler endWork();
}
