//
// Created by BONNe
// Copyright - 2024
//


package lv.id.bonne.vaulthunters.junkcontroller.interfaces;


import org.jetbrains.annotations.Nullable;


/**
 * This interface is used to adjust mixins and allow new method adding inside them.
 */
public interface SearchInterface
{
    /**
     * The method updates search query.
     * @param searchQuery Nullable object of search text.
     */
    void updateSearchQuery(@Nullable String searchQuery);
}
