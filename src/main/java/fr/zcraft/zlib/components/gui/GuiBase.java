/*
 * Copyright or © or Copr. ZLib contributors (2015)
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */

package fr.zcraft.zlib.components.gui;

import org.bukkit.entity.Player;

abstract public class GuiBase 
{
    /**
     * The player this Gui instance is associated to.
     */
    private Player player;
    
    /**
     * The parent of this GUI (if any).
     */
    private GuiBase parent;
    
    /**
     * If the inventory is currently open.
     */
    private boolean open = false;
    
    /**
     * If the GUI is immune to close events.
     * Useful to filter out excessive InventoryCloseEvents Bukkit sends ...
     */
    private boolean immune = false;
    
    /* ===== Public API ===== */
    
    /**
     * Asks the GUI to update its data, and refresh its view accordingly.
     */
    public void update()
    {
        onUpdate();
        onAfterUpdate();
    }
    
    /**
     * Closes this inventory.
     */
    public void close()
    {
        close(false);
    }
    
    /**
     * Closes this inventory.
     * (Dirty hack edition)
     * @param immune if true, the parent of this GUI will be set immune when opened
     */
    protected void close(boolean immune)
    {
        onClose();
        
        if(parent != null)
           Gui.open(player, parent).setImmune(immune);
    }
    
    /* ===== Protected API ===== */
    
    /**
     * Raised when the {@link GuiBase#update()} method is called.
     * Use this method to update your internal data.
     */
    protected void onUpdate() {}
    
    /**
     * Raised when the {@link GuiBase#update()} method is called, but before the inventory is populated.
     * Use this method in a Gui subclass to analyze given data and set other parameters accordingly.
     */
    protected void onAfterUpdate() {}
    
    protected void onClose()
    {
        if(open == false) return;
        open = false;
        Gui.registerGuiClose(this);
    }
    
    protected void open(Player player)
    {
        this.player = player;
        Gui.registerGuiOpen(player, this);
        update();
        open = true;
    }
    
    /* ===== Getters & Setters ===== */
    
    /** @return If the GUI is currently open or not. */
    public final boolean isOpen() { return open; }
    
    /** @return The parent of this GUI. */
    public final GuiBase getParent() { return parent; }
    
    void setParent(GuiBase parent)
    {
        if(parent == this)
            throw new IllegalArgumentException("A GUI cannot be its own parent.");
        this.parent = parent;
    }
    
    /** @return The player this Gui instance is associated to. */
    protected final Player getPlayer() { return player; }
    
    protected boolean checkImmune()
    {
        if(!immune) return false;
        immune = false;
        return true;
    }
    
    private void setImmune(boolean immune)
    {
        this.immune = immune;
    }
    
    /* ===== Static API ===== */
    
}