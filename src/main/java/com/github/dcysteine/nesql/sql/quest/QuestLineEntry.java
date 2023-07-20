package com.github.dcysteine.nesql.sql.quest;

import com.github.dcysteine.nesql.sql.Identifiable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Comparator;

/** Holds information about a BetterQuesting quest line entry. */
@Entity
@EqualsAndHashCode
@ToString
public class QuestLineEntry implements Identifiable<String> {
    @Id
    private String id;

    @Column(nullable = false)
    private int posX;

    @Column(nullable = false)
    private int posY;

    @Column(nullable = false)
    private int sizeX;

    @Column(nullable = false)
    private int sizeY;

    /** Quest line that this quest line entry belongs to. */
    @EqualsAndHashCode.Exclude
    @ManyToOne
    private QuestLine questLine;

    /** Quest that is a part of this quest line entry. */
    @EqualsAndHashCode.Exclude
    @ManyToOne
    private Quest quest;

    /** Needed by Hibernate. */
    protected QuestLineEntry() {}

    public QuestLineEntry(
            String id, int posX, int posY, int sizeX, int sizeY, QuestLine questLine, Quest quest) {
        this.id = id;
        this.posX = posX;
        this.posY = posY;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.questLine = questLine;
        this.quest = quest;
    }

    @Override
    public String getId() { return id; }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public QuestLine getQuestLine() {
        return questLine;
    }

    public Quest getQuest() {
        return quest;
    }

    @Override
    public int compareTo(Identifiable<String> other) {
        if (other instanceof QuestLineEntry) {
            return Comparator.comparing(QuestLineEntry::getId)
                    .compare(this, (QuestLineEntry) other);
        } else {
            return Identifiable.super.compareTo(other);
        }
    }
}
