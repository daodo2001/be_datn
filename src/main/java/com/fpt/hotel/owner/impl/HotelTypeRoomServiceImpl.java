package com.fpt.hotel.owner.impl;

import com.fpt.hotel.config.AppConts;
import com.fpt.hotel.model.*;
import com.fpt.hotel.owner.dto.request.HotelTypeRoomRequest;
import com.fpt.hotel.owner.dto.response.HotelTypeRoomPublicResponse;
import com.fpt.hotel.owner.dto.response.HotelTypeRoomResponse;
import com.fpt.hotel.owner.service.IHotelTypeRoomService;
import com.fpt.hotel.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HotelTypeRoomServiceImpl implements IHotelTypeRoomService {

    @Autowired
    HotelTypeRoomRepository hotelTypeRoomRepository;

    @Autowired
    HotelRepository hotelRepository;

    @Autowired
    TypeRoomRepository typeRoomRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    UtitlityRepository utitlityRepository;

    @Autowired
    RoomUtilityRepository roomUtilityRepository;

    @Override
    @Transactional
    public HotelTypeRoomResponse save(HotelTypeRoomRequest hotelTypeRoomRequest) {
        Long idTypeRoom = hotelTypeRoomRequest.getIdTypeRoom();
        Long idHotel = hotelTypeRoomRequest.getIdHotel();
        Optional<Hotel> hotelOptional = hotelRepository.findById(idHotel);
        Optional<Type_room> typeRoomOptional = typeRoomRepository.findById(idTypeRoom);
        if (hotelOptional.isEmpty() && typeRoomOptional.isEmpty()) {
            return null;
        }

        HotelTypeRoom hotelTypeRoom = new HotelTypeRoom();
        hotelTypeRoom.setTotalNumberRoom(hotelTypeRoomRequest.getTotalNumberRoom());
        hotelTypeRoom.setTypeRoom(typeRoomOptional.get());
        hotelTypeRoom.setHotel(hotelOptional.get());
        HotelTypeRoom newHotelTypeRoom = hotelTypeRoomRepository.save(hotelTypeRoom);

        for (int i = 0; i < newHotelTypeRoom.getTotalNumberRoom(); i++) {
            Room room = new Room();
            room.setTypeRoom(newHotelTypeRoom.getTypeRoom());
            room.setStatus(AppConts.STILLEMPTY);
            room.setEnabled(true);
            room.setHotel(newHotelTypeRoom.getHotel());
            room.setDescription(AppConts.STILLEMPTY);
            if (newHotelTypeRoom.getTypeRoom().getName().equalsIgnoreCase(AppConts.DOUBLEROOM)) {
                room.setNumberRoom("L0" + i + 1);
            } else if (newHotelTypeRoom.getTypeRoom().getName().equalsIgnoreCase(AppConts.SINGLEROOM)) {
                room.setNumberRoom("S0" + i + 1);
            } else if (newHotelTypeRoom.getTypeRoom().getName().equalsIgnoreCase(AppConts.VIPROOM)) {
                room.setNumberRoom("V0" + i + 1);
            }else {
                room.setNumberRoom("P0" + i + 1);
            }
            Room newRoom = roomRepository.save(room);
            this.saveRoomUtility(newRoom);
        }

        return modelMapper.map(newHotelTypeRoom, HotelTypeRoomResponse.class);
    }

    private void saveRoomUtility(Room newRoom) {
        List<Utility> utilityList = utitlityRepository.findAll();
        for (Utility utility : utilityList) {
            if (utility.getName().equalsIgnoreCase("Điều hòa") ||
                    utility.getName().equalsIgnoreCase("Nóng lạnh")) {
                RoomUtility roomUtility = new RoomUtility();
                roomUtility.setRoom(newRoom);
                roomUtility.setQuantity(1);
                roomUtility.setUtility(utility);
                RoomUtility newRoomUtility = roomUtilityRepository.save(roomUtility);
                roomUtilityRepository.save(newRoomUtility);
            }
        }
    }


    @Override
    public List<HotelTypeRoomResponse> findAll(Long idHotel) {
        List<HotelTypeRoom> hotelTypeRooms = new ArrayList<>();
        if(idHotel != null){
            hotelTypeRooms = hotelTypeRoomRepository.findAll(idHotel);
        }else {
            hotelTypeRooms = hotelTypeRoomRepository.findAll();
        }
        return hotelTypeRooms.stream().map(
                hotelTypeRoom -> modelMapper.map(hotelTypeRoom, HotelTypeRoomResponse.class)).collect(Collectors.toList());
    }

    @Override
    public List<HotelTypeRoomResponse> checkHotelTypeRoom(HotelTypeRoomRequest hotelTypeRoomRequest) {
        return hotelTypeRoomRepository.checkHotelTypeRoom(hotelTypeRoomRequest.getIdHotel(),
                hotelTypeRoomRequest.getIdTypeRoom()).stream().map(hotelTypeRoom ->
                modelMapper.map(hotelTypeRoom, HotelTypeRoomResponse.class)).collect(Collectors.toList());
    }

    @Override
    public List<HotelTypeRoomPublicResponse> hotelTypeRoomPublicResponses(
            Long idHotel, Long idTypeRoom,
            String dateIn, String dateOut) {
        List<HotelTypeRoom> hotelTypeRooms = hotelTypeRoomRepository.hotelTypeRoomFindByIdHotel(idHotel);
        //kiểm tra phòng đang bảo trì
        List<Room> rooms = roomRepository.findRoomByEnabled(idHotel, idTypeRoom);

        if (dateIn != null && dateOut != null && idTypeRoom != null) {
            List<Booking> bookings = bookingRepository.checkTotalRoom(idHotel, idTypeRoom, dateIn, dateOut);
            List<Booking> checkTotalRoomByUsing = bookingRepository.checkTotalRoomByUsing(idHotel, idTypeRoom, dateIn, dateOut);
            Integer totalRoom = 0;
            for (Booking booking : bookings) {
                totalRoom += booking.getTotalRooms();
            }
            for (Booking booking : checkTotalRoomByUsing) {
                totalRoom += booking.getTotalRooms();
            }

            for (HotelTypeRoom typeRoom : hotelTypeRooms) {
                if (typeRoom.getTypeRoom().getId() == idTypeRoom
                        && typeRoom.getHotel().getId() == idHotel) {
                    Integer newTotalNumberRoom = typeRoom.getTotalNumberRoom() - totalRoom;
                    typeRoom.setTotalNumberRoom(newTotalNumberRoom < 0 ? 0 : newTotalNumberRoom);
                }
            }
        }

        // set lại số phòng hiện có cho khách sạn
        for (HotelTypeRoom typeRoom : hotelTypeRooms) {
            if (typeRoom.getTypeRoom().getId() == idTypeRoom
                    && typeRoom.getHotel().getId() == idHotel) {
                Integer newTotalNumberRoom = typeRoom.getTotalNumberRoom() - rooms.size();
                typeRoom.setTotalNumberRoom(newTotalNumberRoom < 0 ? 0 : newTotalNumberRoom);
            }
        }

        return hotelTypeRooms.stream().map(
                hotelTypeRoom -> modelMapper.map(hotelTypeRoom, HotelTypeRoomPublicResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public HotelTypeRoomResponse update(HotelTypeRoomRequest hotelTypeRoomRequest) {

        Long idHotel = hotelTypeRoomRequest.getIdHotel();
        Long idTypeRoom = hotelTypeRoomRequest.getIdTypeRoom();
        List<Room> rooms = roomRepository.findAll(idHotel, idTypeRoom);
        HotelTypeRoom hotelTypeRoom = hotelTypeRoomRepository.checkHotelTypeRoom(idHotel, idTypeRoom).get(0);
        List<RoomUtility> utilityList = new ArrayList<>();
        if (hotelTypeRoom.getTotalNumberRoom() > hotelTypeRoomRequest.getTotalNumberRoom()) {
            for (int i = rooms.size(); i > hotelTypeRoomRequest.getTotalNumberRoom(); i--) {
                Room room = rooms.get(i - 1);
                if (room.getStatus().equalsIgnoreCase(AppConts.USING)) {
                    return null;
                } else {
                    utilityList = roomUtilityRepository.findAllByIdRoom(room.getId());
                    roomUtilityRepository.deleteAll(utilityList);
                    roomRepository.delete(room);
                }
            }
        } else {
            for (int i = rooms.size(); i < hotelTypeRoomRequest.getTotalNumberRoom(); i++) {
                Room room = new Room();
                Type_room typeRoom = typeRoomRepository.findById(hotelTypeRoomRequest.getIdTypeRoom()).get();
                room.setTypeRoom(typeRoomRepository.findById(hotelTypeRoomRequest.getIdTypeRoom()).get());
                room.setHotel(hotelRepository.findById(hotelTypeRoomRequest.getIdHotel()).get());
                room.setEnabled(true);
                room.setDescription(AppConts.STILLEMPTY);
                room.setStatus(AppConts.STILLEMPTY);
                if (typeRoom.getName().equalsIgnoreCase(AppConts.DOUBLEROOM)) {
                    room.setNumberRoom("L0" + i + 1);
                } else if (typeRoom.getName().equalsIgnoreCase(AppConts.SINGLEROOM)) {
                    room.setNumberRoom("S0" + i + 1);
                } else if (typeRoom.getName().equalsIgnoreCase(AppConts.VIPROOM)) {
                    room.setNumberRoom("V0" + i + 1);
                }
                else {
                    room.setNumberRoom("P0" + i + 1);
                }
                Room newRoom = roomRepository.save(room);
                this.saveRoomUtility(newRoom);
            }
        }
        hotelTypeRoom.setTotalNumberRoom(hotelTypeRoomRequest.getTotalNumberRoom());
        return modelMapper.map(hotelTypeRoom, HotelTypeRoomResponse.class);
    }
}
