package io.pivotal.pal.tracker;



import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class JdbcTimeEntryRepository implements TimeEntryRepository {

    private JdbcTemplate template;
    private long timeEntryId;

    public JdbcTimeEntryRepository(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry entry) {
        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();

            template.update( connection -> {
                PreparedStatement statement = connection.prepareStatement("INSERT INTO time_entries (project_id, user_id, date, hours) VALUES (?, ?, ?, ?)", RETURN_GENERATED_KEYS);
                statement.setLong(1, entry.getProjectId());
                statement.setLong(2, entry.getUserId());
                statement.setDate(3, Date.valueOf(entry.getDate()));
                statement.setInt(4, entry.getHours());

                return statement;
            }, generatedKeyHolder);

        return find(generatedKeyHolder.getKey().longValue());
    }

    @Override
    public TimeEntry find(long timeEntryId) {
        return template.query(
                "SELECT id, project_id, user_id, date, hours FROM time_entries WHERE id = ?",
                new Object[]{timeEntryId},
                extractor);
    }

    @Override
    public List<TimeEntry> list() {
        List<TimeEntry> listEntries = template.query("SELECT id, project_id, user_id, date, hours FROM time_entries", mapper);
        return listEntries;
    }

    @Override
    public TimeEntry update(long timeEntryId, TimeEntry entry) {


        template.update("UPDATE time_entries " +
                "SET project_id = ?, user_id = ?, date = ?,  hours = ? " +
                "WHERE id = ?", entry.getProjectId(), entry.getUserId(), Date.valueOf(entry.getDate()), entry.getHours(), timeEntryId);

        return find(timeEntryId);
    }

    @Override
    public void delete(long timeEntryId) {
        template.update("DELETE from time_entries where id = ?", timeEntryId);
    }

    private final RowMapper<TimeEntry> mapper = (rs, rowNum) -> new TimeEntry(
            rs.getLong("id"),
            rs.getLong("project_id"),
            rs.getLong("user_id"),
            rs.getDate("date").toLocalDate(),
            rs.getInt("hours")
    );

    private final ResultSetExtractor<TimeEntry> extractor = (rs) -> rs.next() ? mapper.mapRow(rs, 1) : null;
}
