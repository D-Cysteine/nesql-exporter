package com.github.dcysteine.nesql.sql.quest;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.Metadata;
import com.github.dcysteine.nesql.sql.base.item.Item;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Holds information about a BetterQuesting quest. */
@Entity
@EqualsAndHashCode
@ToString
public class Quest implements Identifiable<String> {
    @Id
    private String id;

    /** The quest ID in the BetterQuesting database. */
    @Column(nullable = false)
    private String questId;

    @ManyToOne
    private Item icon;

    @Column(nullable = false)
    private String name;

    @Column(length = Metadata.MAX_STRING_LENGTH, nullable = false)
    private String description;

    @Column(nullable = false)
    private String visibility;

    private int repeatTime;

    /** Quest line entries that this quest belongs to. */
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "quest")
    private Set<QuestLineEntry> questLineEntries;

    @Column(nullable = false)
    private String questLogic;

    /** Quests that this quest requires. */
    @EqualsAndHashCode.Exclude
    @ManyToMany
    private Set<Quest> requiredQuests;

    /** Quests that require this quest. */
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "requiredQuests")
    private Set<Quest> requiredByQuests;

    @Column(nullable = false)
    private String taskLogic;

    @OneToMany
    @OrderColumn
    private List<Task> tasks;

    @OneToMany
    @OrderColumn
    private List<Reward> rewards;

    /** Needed by Hibernate. */
    protected Quest() {}

    /**
     * We'll set {@link #requiredQuests} in a second pass,
     * after all quests have been committed to the DB.
     */
    public Quest(
            String id, String questId, Item icon, String name, String description,
            String visibility, int repeatTime,
            String questLogic, String taskLogic, List<Task> tasks, List<Reward> rewards) {
        this.id = id;
        this.questId = questId;
        this.icon = icon;
        this.name = name;
        this.description = description;
        this.visibility = visibility;
        this.repeatTime = repeatTime;
        this.questLogic = questLogic;
        this.taskLogic = taskLogic;
        this.requiredQuests = new HashSet<>();
        this.tasks = tasks;
        this.rewards = rewards;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getQuestId() {
        return questId;
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

    public int getRepeatTime() {
        return repeatTime;
    }

    public Set<QuestLineEntry> getQuestLineEntries() {
        return questLineEntries;
    }

    public String getQuestLogic() {
        return questLogic;
    }

    public Set<Quest> getRequiredQuests() {
        return requiredQuests;
    }

    public void setRequiredQuests(Set<Quest> requiredQuests) {
        this.requiredQuests = requiredQuests;
    }

    public Set<Quest> getRequiredByQuests() {
        return requiredByQuests;
    }

    public String getTaskLogic() {
        return taskLogic;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public List<Reward> getRewards() {
        return rewards;
    }

    @Override
    public int compareTo(Identifiable<String> other) {
        if (other instanceof Quest) {
            return Comparator.comparing(Quest::getName)
                    .thenComparing(Quest::getId)
                    .compare(this, (Quest) other);
        } else {
            return Identifiable.super.compareTo(other);
        }
    }
}
