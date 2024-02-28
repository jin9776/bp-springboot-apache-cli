package org.freehan.conf;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter @Setter
public class Configure {

    @Value("${remote.host}")
    private String remoteHost;

    @Value("${remote.port}")
    private int remotePort;

    private String command;

    private final String[] commandList = new String[] { "phone", "phoneextn"};
}
