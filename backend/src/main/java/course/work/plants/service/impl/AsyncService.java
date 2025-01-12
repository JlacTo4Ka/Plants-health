package course.work.plants.service.impl;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
public class AsyncService {

    @Async
    public <T> void execute(Supplier<T> action) {
        action.get();
    }
}
