package com.github.dcysteine.nesql.sql.quest;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

/** Holds information about a BetterQuesting quest line entry. */
@Embeddable
@EqualsAndHashCode
@Getter
@ToString
public class QuestLineEntry implements Comparable<QuestLineEntry> {
    @ManyToOne
    private Quest quest;

    private int posX;
    private int posY;
    private int sizeX;
    private int sizeY;

    /** Needed by Hibernate. */
    protected QuestLineEntry() {}

    public QuestLineEntry(Quest quest, int posX, int posY, int sizeX, int sizeY) {
        this.quest = quest;
        this.posX = posX;
        this.posY = posY;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    @Override
    public int compareTo(@NotNull QuestLineEntry other) {
        return Comparator.comparing(QuestLineEntry::getQuest)
                .thenComparing(QuestLineEntry::getPosX)
                .thenComparing(QuestLineEntry::getPosY)
                .thenComparing(QuestLineEntry::getSizeX)
                .thenComparing(QuestLineEntry::getSizeY)
                .compare(this, other);
    }
}
