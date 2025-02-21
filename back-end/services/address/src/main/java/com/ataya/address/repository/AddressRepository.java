package com.ataya.address.repository;

import com.ataya.address.enums.AddressTag;
import com.ataya.address.model.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface AddressRepository extends MongoRepository<Address, String> {
    Optional<Address> findByLatAndLng(Double lat, Double lng);

    Page<Address> findByAddressTagsIn(Collection<List<AddressTag>> addressTags, Pageable pageable);

    Optional<Address> findByAddressId(String addressId);

    @Query(
            value = "{ " +
                        "'location': { " +
                            "$nearSphere: { " +
                                "$geometry: { " +
                                    "type: 'Point', coordinates: [?1, ?0] " +
                                "}, $maxDistance: ?2 " +
                            "} " +
                        "}, " +
                        "'addressTags': { $in: ?3 } " +
                    "}"
    )
    Page<Address> findNearbyAddressesByLocationAndAddressTagsIn(double lat, double lng, double distance, Collection<List<AddressTag>> addressTags, Pageable pageable);

    @Query(
            value = "{ " +
                        "'location': { " +
                            "$nearSphere: { " +
                                "$geometry: { " +
                                    "type: 'Point', coordinates: [?1, ?0] " +
                                "}, $maxDistance: ?2 " +
                            "} " +
                        "} " +
                    "}"
    )
    Page<Address> findNearbyAddressesByLocation(Double lat, Double lng, Integer distance, Pageable pageable);
}
