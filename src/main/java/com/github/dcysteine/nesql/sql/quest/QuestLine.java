package com.github.dcysteine.nesql.sql.quest;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.Metadata;
import com.github.dcysteine.nesql.sql.base.item.Item;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Comparator;
import java.util.Set;

/** Holds information about a BetterQuesting quest line. */
@Entity
@EqualsAndHashCode
@ToString
public class QuestLine implements Identifiable<String> {
    @Id
    private String id;

    /** The quest line ID in the BetterQuesting database. */
    @Column(nullable = false)
    private String questLineId;

    @ManyToOne
    private Item icon;

    @Column(nullable = false)
    private String name;

    @Column(length = Metadata.MAX_STRING_LENGTH, nullable = false)
    private String description;

    @Column(nullable = false)
    private String visibility;

    /** Quest line entries that are part of this quest line. */
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "questLine")
    private Set<QuestLineEntry> questLineEntries;

    /** Needed by Hibernate. */
    protected QuestLine() {}

    public QuestLine(
            String id, String questLineId, Item icon, String name, String description,
            String visibility, Set<QuestLineEntry> questLineEntries) {
        this.id = id;
        this.questLineId = questLineId;
        this.icon = icon;
        this.name = name;
        this.description = description;
        this.visibility = visibility;
        this.questLineEntries = questLineEntries;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getQuestLineId() {
        return questLineId;
    }

    public Item getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getVisibility() {
        return visibility;
    }

    public Set<QuestLineEntry> getQuestLineEntries() {
        return questLineEntries;
    }

    @Override
    public int compareTo(Identifiable<String> other) {
        if (other instanceof QuestLine) {
            return Comparator.comparing(QuestLine::getName)
                    .thenComparing(QuestLine::getId)
                    .compare(this, (QuestLine) other);
        } else {
            return Identifiable.super.compareTo(other);
        }
    }
}
