package io.javabrains.inbox.folders;

import org.springframework.data.annotation.QueryAnnotation;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import java.util.List;

public interface UnreadEmailStatsRepository extends CassandraRepository<UnreadEmailStats, String> {
    List<UnreadEmailStats> findAllById(String Id);

    @Query("update unread_email_stats set unreadcount= unreadCount+1 where user_id=?0 and label=?1;")
    public void incrementUnreadCounter(String userId, String label);
    @Query("update unread_email_stats set unreadcount= unreadCount-1 where user_id=?0 and label=?1;")

    public void decrementUnreadCounter(String userId, String label);

}
