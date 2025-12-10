package model.manager;

import model.entity.Guest;
import java.io.Serializable;
import java.util.*;

public class GuestManager implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Guest> guests;

    public GuestManager() {
        this.guests = new HashMap<>();
    }

    public boolean addGuest(Guest guest) {
        if (guest != null && !guests.containsKey(guest.getId())) {
            guests.put(guest.getId(), guest);
            return true;
        }
        return false;
    }

    public boolean removeGuest(String guestId) {
        return guests.remove(guestId) != null;
    }

    public Guest getGuest(String guestId) {
        return guests.get(guestId);
    }

    public List<Guest> getAllGuests() {
        return new ArrayList<>(guests.values());
    }
}