package tr.com.havelsan.dynamicobject.common.sequence;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class SequenceGeneratorService {
    private final MongoOperations mongoOperations;
    private final Map<String, SequenceBlock> sequenceBlocks = new ConcurrentHashMap<>();
    private final int allocationSize;

    public SequenceGeneratorService(
            MongoOperations mongoOperations,
            @Value("${app.sequence.allocation-size:50}") int allocationSize
    ) {
        this.mongoOperations = mongoOperations;
        this.allocationSize = Math.max(allocationSize, 1);
    }

    public int getNextSequence(String seqName) {
        SequenceBlock block = sequenceBlocks.computeIfAbsent(seqName, key -> new SequenceBlock());
        synchronized (block) {
            if (block.next > block.max) {
                DatabaseSequence sequence = mongoOperations.findAndModify(
                        Query.query(Criteria.where("_id").is(seqName)),
                        new Update().inc("seq", allocationSize),
                        FindAndModifyOptions.options().returnNew(true).upsert(true),
                        DatabaseSequence.class
                );
                long maxValue = sequence == null ? allocationSize : sequence.getSeq();
                block.next = maxValue - allocationSize + 1;
                block.max = maxValue;
            }
            return (int) block.next++;
        }
    }

    private static class SequenceBlock {
        private long next = 1;
        private long max = 0;
    }
}
