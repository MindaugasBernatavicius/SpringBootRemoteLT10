package cf.mindaugas.springbootremotelt10;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DummyLogger {
    public void doSomething(){
        log.info(">>>> Hi");
    }
}
