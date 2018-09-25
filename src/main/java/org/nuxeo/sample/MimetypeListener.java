package org.nuxeo.sample;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.RecoverableClientException;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventContext;
import org.nuxeo.ecm.core.event.EventListener;
import org.nuxeo.ecm.core.event.EventBundle;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;

public class MimetypeListener implements EventListener {
  
    private static final Log log = LogFactory.getLog(MimetypeListener.class);

    protected final List<String> handled = Arrays.asList("emptyDocumentModelCreated", "aboutToCreate", "beforeDocumentModification", "documentModified");

    public void handleEvent(EventBundle events) {
        for (Event event : events) {
            if (acceptEvent(event)) {
                handleEvent(event);
            }
        }
    }

    public boolean acceptEvent(Event event) {
        return handled.contains(event.getName());
    }

  
    public void handleEvent(Event event) {
        EventContext ctx = event.getContext();
        if (!(ctx instanceof DocumentEventContext)) {
          return;
        }

        DocumentEventContext docCtx = (DocumentEventContext) ctx;
        DocumentModel doc = docCtx.getSourceDocument();
        String mimeType = new String();

        try {
            if (doc.getType().contains("File")){
                log.error(doc.getName()+" "+event.getName());
                log.error(doc.toString());
                Blob blob = (Blob) doc.getPropertyValue("file:content");
                if (blob != null) {
                    mimeType = blob.getMimeType();
                    log.error(mimeType); 
            //event.markRollBack();
            //throw new RecoverableClientException("error","error",null);
                }
            } else {
            }
        } catch (RecoverableClientException e) {
            log.error("caught exception");
        }

    }
//    public void handleExcep(RecoverableClientException e, Event event){
    //public void handleExcep(Exception e, Event event){
//        String msg = "Current event " + event.getName() + " does not match mimetype, rolling back";
//        log.info(msg);
        //e.addInfo(msg);
    //}

}
