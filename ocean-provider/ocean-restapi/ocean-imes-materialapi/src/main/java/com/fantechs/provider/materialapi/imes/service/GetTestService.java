package com.fantechs.provider.materialapi.imes.service;


import javax.xml.rpc.ServiceException;
import java.io.IOException;
import java.rmi.RemoteException;

public interface GetTestService {

    String getApache();

    String getHttp() throws IOException;

    String getApacheMes();

    String getLeisai();

    public String getAxis2() throws ServiceException, RemoteException;
}
