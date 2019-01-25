package org.springframework.data.rest.shell.context;

import org.springframework.context.ApplicationEvent;

import java.net.URI;

/**
 * @author Jon Brisbin
 */
public class BaseUriChangedEvent extends ApplicationEvent {

  public BaseUriChangedEvent(URI baseUri) {
    super(baseUri);
  }

  public URI getBaseUri() {
    return (URI)getSource();
  }

}
