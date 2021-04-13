package com.stupica.queue;


import com.stupica.ConstGlobal;
import com.stupica.core.UtilString;

import javax.jms.MapMessage;
import javax.jms.Message;
import java.util.logging.Logger;


public class JmsClientMail extends JmsClientBase {

    public static final String	sKeyFromEMail	= "from";
    public static final String	sKeyFromName	= "fromName";
    public static final String	sKeyToEMail		= "to";
    public static final String	sKeyToName		= "toName";
    public static final String	sKeyCcEMail		= "cc";
    public static final String	sKeyCcName		= "ccName";
    public static final String	sKeySubject		= "subject";
    public static final String	sKeyContent		= "content";
    public static final String	sKeyContentType	= "contentType";

    private static Logger logger = Logger.getLogger(JmsClientMail.class.getName());


    /**
     * Object constructor
     */
    public JmsClientMail() {
        super();
        iMsgTTL = 1000 * 60 * 60 * 24 * 61;
        //sQueueAddr = "tcp://localhost:61616";
        sQueueName = "all.mail2send.queue";
        sClientId = "mailConsumer.programId";
    }


    /**
     * Method: receiveEMail
     *
     * Read ..
     *
     * @return Map sMsg	notNull = AllOK;
     */
    public MapMessage receiveEMail(int aiQueueWaitTime) {
    //public Map receiveEMail(int aiQueueWaitTime) {
        // Local variables
        int             iResult;
        //HashMap         objData = null;
        Message         objMessage;
        MapMessage      objQuMsg = null;

        // Initialization
        iResult = ConstGlobal.RETURN_OK;

        objMessage = receiveCommon(aiQueueWaitTime);
        if (objMessage != null) {
            if (objMessage instanceof MapMessage) {
                objQuMsg = (MapMessage)objMessage;
                /*
                try {
                    objData = new HashMap();
                    objData.put(sKeyFromEMail, objQuMsg.getString(sKeyFromEMail));
                    if (objQuMsg.itemExists(sKeyFromName))
                        objData.put(sKeyFromName, objQuMsg.getString(sKeyFromName));
                    objData.put(sKeyToEMail, objQuMsg.getString(sKeyToEMail));
                    if (objQuMsg.itemExists(sKeyToName))
                        objData.put(sKeyToName, objQuMsg.getString(sKeyToName));

                    if (objQuMsg.itemExists(sKeyCcEMail))
                        objData.put(sKeyCcEMail, objQuMsg.getString(sKeyCcEMail));
                    if (objQuMsg.itemExists(sKeyCcName))
                        objData.put(sKeyCcName, objQuMsg.getString(sKeyCcName));

                    if (objQuMsg.itemExists(sKeySubject))
                        objData.put(sKeySubject, objQuMsg.getString(sKeySubject));
                    if (objQuMsg.itemExists(sKeyContent))
                        objData.put(sKeyContent, objQuMsg.getString(sKeyContent));
                    if (objQuMsg.itemExists(sKeyContentType))
                        objData.put(sKeyContentType, objQuMsg.getString(sKeyContentType));
                } catch (Exception ex) {
                    iResult = ConstGlobal.RETURN_ERROR;
                    logger.severe("receiveEMail(): Error at message operation (extraction)!"
                            + " Operation: ?"
                            + "; Msg.: " + ex.getMessage());
                } */
            } else {
                logger.warning("receiveEMail(): = Message of unknown Type! Ignoring ..");
            }
        }
        return objQuMsg;
    }


    public int sendEMail(String asTo, String asSubject, String asContent) {
        return sendEMail(null, asTo, asSubject, asContent, null);
    }
    /**
     * Method: sendEMail
     *
     * Send ..
     *
     * @return int	1 = AllOK;
     */
    public int sendEMail(String asFrom, String asTo, String asSubject, String asContent, String asContentType) {
        // Local variables
        int             iResult;
        MapMessage      objMessage = null;

        // Initialization
        iResult = ConstGlobal.RETURN_OK;

        try {
            objMessage = getSession().createMapMessage();
            if (!UtilString.isEmptyTrim(asFrom))
                objMessage.setString(sKeyFromEMail, asFrom);
            objMessage.setString(sKeyToEMail, asTo);
            objMessage.setString(sKeySubject, asSubject);
            objMessage.setString(sKeyContent, asContent);
            if (!UtilString.isEmptyTrim(asContentType))
                objMessage.setString(sKeyContentType, asContentType);
            objMessage.setJMSType("Map");
        } catch (Exception ex) {
            iResult = ConstGlobal.RETURN_ERROR;
            logger.severe("sendEMail(): Error at message send!"
                    + " URI: " + sQueueAddr
                    + "; Queue: " + sQueueName
                    + "; Msg.: " + ex.getMessage());
            //ex.printStackTrace();
        }
        if (objMessage != null) {
            try {
                getProducer().send(objMessage);
            } catch (Exception ex) {
                iResult = ConstGlobal.RETURN_ERROR;
                logger.severe("sendEMail(): Error at message send!"
                        + " URI: " + sQueueAddr
                        + "; Queue: " + sQueueName
                        + "; Msg.: " + ex.getMessage());
                //ex.printStackTrace();
            }
        }
        return iResult;
    }
}
