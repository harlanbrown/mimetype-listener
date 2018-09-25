package org.nuxeo.sample;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.event.EventService;
import org.nuxeo.ecm.core.event.impl.EventListenerDescriptor;
import org.nuxeo.ecm.platform.test.PlatformFeature;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.Blobs;

import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import org.nuxeo.ecm.core.api.IdRef;

import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;

@RunWith(FeaturesRunner.class)
@Features({ PlatformFeature.class })
@Deploy({"org.nuxeo.sample.mimetype-listener-core"})
public class TestMimetypeListener {

    private static final Log log = LogFactory.getLog(TestMimetypeListener.class);

//    protected final List<String> events = Arrays.asList("emptyDocumentModelCreated", "aboutToCreate", "beforeDocumentModification");
    protected final List<String> events = Arrays.asList("beforeDocumentModification");

    @Inject
    protected CoreSession session;

    @Inject
    protected EventService s;

    String docId;

    @Before 
    public void setUp() {
        DocumentModel ws1 = session.createDocumentModel("/default-domain/workspaces", "ws1", "Workspace");
        ws1 = session.createDocument(ws1);

        DocumentModel doc = session.createDocumentModel("/default-domain/workspaces/ws1", "default", "File");
        doc = session.createDocument(doc);
        session.save();
        docId = doc.getId();
    }

    @Test
    public void listenerRegistration() {
        EventListenerDescriptor listener = s.getEventListener("mimetypelistener");
        assertNotNull(listener);
        assertTrue(events.stream().allMatch(listener::acceptEvent));
    }

    @Test
    public void listenForBlobAttachment() {
        EventListenerDescriptor listener = s.getEventListener("mimetypelistener");
        assertNotNull(listener);

        DocumentModel doc = session.getDocument(new IdRef(docId));
        Blob blob = Blobs.createBlob("Dummy txt", "text/plain", null, "dummy.txt");
        doc.setPropertyValue("file:content", (Serializable) blob);
        session.save();
//        Blob blob2 = (Blob) doc.getPropertyValue("file:content");
//        assertNotNull(blob2);
    }

}
