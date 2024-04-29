package com.github.dcysteine.nesql.sql.quest;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.Metadata;
import com.github.dcysteine.nesql.sql.base.item.Item;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Comparator;
import java.util.HashSet;
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

    /** Quests in this quest line. */
    @EqualsAndHashCode.Exclude
    @ManyToMany
    private Set<Quest> quests;

    /** Quest line entries in this quest line. Use this if you want to draw the quest line. */
    @ElementCollection
    private Set<QuestLineEntry> questLineEntries;

    /** Needed by Hibernate. */
    protected QuestLine() {}

    public QuestLine(
            String id, String questLineId, Item icon, String name, String description,
            String visibility) {
        this.id = id;
        this.questLineId = questLineId;
        this.icon = icon;
        this.name = name;
        this.description = description;
        this.visibility = visibility;

        quests = new HashSet<>();
        questLineEntries = new HashSet<>();
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

    public Set<Quest> getQuests() {
        return quests;
    }

    public void addQuest(Quest quest) {
        quests.add(quest);
    }

    public Set<QuestLineEntry> getQuestLineEntries() {
        return questLineEntries;
    }

    public void addQuestLineEntry(QuestLineEntry entry) {
        questLineEntries.add(entry);
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
