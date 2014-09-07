package rf.protocols.core.message;

import rf.protocols.core.Message;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public interface RollingIdMessage extends Message {
    int getRollingId();
}
