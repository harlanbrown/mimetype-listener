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

    protected final List<String> handled = Arrays.asList("emptyDocumentModelCreated", "aboutToCreate", "beforeDocumentModification");

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
        log.error("HELLO HELLO");

        DocumentEventContext docCtx = (DocumentEventContext) ctx;
        DocumentModel doc = docCtx.getSourceDocument();
        String mimeType = new String();

        // Add some logic starting from here.

        try {
            Blob blob = (Blob) doc.getPropertyValue("file:content");
            mimeType = blob.getMimeType();
        } catch (Exception e) {
        }

        log.error(mimeType);

        handleExcep(new RecoverableClientException("error","error",null), event);

    }
    public void handleExcep(RecoverableClientException e, Event event){
        String msg = "Current event " + event.getName() + " would break Quota restriction, rolling back";
        log.error(msg);
        log.info(msg);
        e.addInfo(msg);
        event.markRollBack("Quota Exceeded", e);
    }

}
