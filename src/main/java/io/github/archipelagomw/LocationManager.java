package io.github.archipelagomw;

import io.github.archipelagomw.network.client.LocationChecks;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LocationManager {

    // TODO: why is this field unused?
    private final Client client;
    private WebSocket webSocket;

    private final Set<Long> checkedLocations = Collections.newSetFromMap(new ConcurrentHashMap<>());

    private final Set<Long> missingLocations = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public LocationManager(Client client) {
        this.client = client;
    }

    public APResult<Void> checkLocation(long id) {
        return checkLocations(new ArrayList<Long>(1) {{add(id);}});
    }

    public APResult<Void> checkLocations(Collection<Long> ids) {
        ids.removeIf( location -> !missingLocations.contains(location));
        checkedLocations.addAll(ids);
        missingLocations.removeAll(ids);
        LocationChecks packet = new LocationChecks();
        packet.locations.addAll(ids);
        APResult<Void> ret = client.ensureConnectedAndAuth();
        if(ret.getCode() == APResult.ResultCode.SUCCESS)
        {
            webSocket.sendPacket(packet);
            ret = APResult.success();
        }
        return ret;
    }

    public APResult<Void> sendIfChecked(Set<Long> missingChecks) {
        LocationChecks packet = new LocationChecks();
        packet.locations = new HashSet<>();
        for (Long missingCheck : missingChecks) {
            if(checkedLocations.contains(missingCheck)) {
                packet.locations.add(missingCheck);
            }
        }
        if(packet.locations.isEmpty())
        {
            return APResult.success();
        }
        APResult<Void> ret = client.ensureConnectedAndAuth();
        if(ret.getCode() == APResult.ResultCode.SUCCESS)
        {
            webSocket.sendPacket(packet);
            ret = APResult.success();
        }
        return ret;
    }

    public APResult<Void> resendAllCheckedLocations() {
        LocationChecks packet = new LocationChecks();
        packet.locations = checkedLocations;
        APResult<Void> ret = client.ensureConnectedAndAuth();
        if(ret.getCode() == APResult.ResultCode.SUCCESS)
        {
            webSocket.sendPacket(packet);
            ret = APResult.success();
        }
        return ret;
    }

    void setAPWebSocket(WebSocket webSocket) {
        this.webSocket = webSocket;
    }

    public Set<Long> getCheckedLocations() {
        return checkedLocations;
    }

    public Set<Long> getMissingLocations() {
        return missingLocations;
    }

    public void addCheckedLocations(Set<Long> newLocations) {
        this.checkedLocations.addAll(newLocations);
        this.missingLocations.removeAll(newLocations);
    }

    public void setMissingLocations(Set<Long> missingLocations) {
        this.missingLocations.clear();
        this.missingLocations.addAll(missingLocations);
    }

    /**
    Helper to get the location ID from an inputted location name as a String. Only use this
    if you know what you're dealing with, and can accept that getting this from the datapackage
    is not 100% reliable.

     @param locationName The name of the location you wish to look up.
     @return The ID of the location that you have looked up from the datapackage.
     */
    public Optional<Long> getLocationNameFromID(String locationName){
        return Optional.ofNullable(
                this.client
                        .getDataPackage()
                        .getGame(this.client.getGame())
                 )
                 .map(game -> game.locationNameToId
                        .get(locationName)
                 )
        );
    }
}
