package com.fpt.hotel.repository;

import com.fpt.hotel.model.Transaction_Info;
import com.fpt.hotel.owner.dto.response.ChartResponse;
import com.fpt.hotel.owner.dto.response.ColumnChart;
import com.fpt.hotel.owner.dto.response.Total;
import com.fpt.hotel.staff.dto.response.OrderDetailsResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionInfoRepository extends JpaRepository<Transaction_Info , Long> {

    @Query(value = "SELECT * FROM `transaction_infos` WHERE id_booking = ?1" , nativeQuery = true)
    Transaction_Info findByIdBooking(Long idBooking);

    @Query(value = "SELECT transaction_infos.id , services.name , transaction_service.quantity , services.price as totalPrice  FROM `transaction_infos` \n" +
            "join transaction_service on transaction_service.id_transaction = transaction_infos.id\n" +
            "join services on services.id = transaction_service.id_service_hotel\n" +
            "where transaction_infos.id = ?1",nativeQuery = true)
    List<OrderDetailsResponse> orderDetails(Long idTransaction);

    @Query(value = "SELECT hotels.name,SUM(transaction_infos.total_price) as total  FROM transaction_infos \n" +
            "   join bookings on bookings.id = transaction_infos.id_booking\n" +
            "   join hotels on hotels.id = bookings.id_hotel\n" +
            "   where hotels.id = ?1 " , nativeQuery = true)
    ChartResponse chartResponse(Long idHotel);

    @Query(value = "SELECT SUM(transaction_infos.total_price) as total  FROM transaction_infos ",nativeQuery = true)
    Total totalDoanhThu();

    @Query(value = "SELECT \n" +
            "    SUM(IF(month = 'Jan', total, 0)) AS 'Jan',\n" +
            "    SUM(IF(month = 'Feb', total, 0)) AS 'Feb',\n" +
            "    SUM(IF(month = 'Mar', total, 0)) AS 'Mar',\n" +
            "    SUM(IF(month = 'Apr', total, 0)) AS 'Apr',\n" +
            "    SUM(IF(month = 'May', total, 0)) AS 'May',\n" +
            "    SUM(IF(month = 'Jun', total, 0)) AS 'Jun',\n" +
            "    SUM(IF(month = 'Jul', total, 0)) AS 'Jul',\n" +
            "    SUM(IF(month = 'Aug', total, 0)) AS 'Aug',\n" +
            "    SUM(IF(month = 'Sep', total, 0)) AS 'Sep',\n" +
            "    SUM(IF(month = 'Oct', total, 0)) AS 'Oct',\n" +
            "    SUM(IF(month = 'Nov', total, 0)) AS 'Nov',\n" +
            "    SUM(IF(month = 'Dec', total, 0)) AS 'Dec'\n" +
            "FROM\n" +
            "    (SELECT MIN(DATE_FORMAT(date_release, '%b')) AS month, SUM(transaction_infos.total_price) AS total\n" +
            "    FROM transaction_infos \n" +
            "    join bookings ON bookings.id = transaction_infos.id_booking\n" +
            "    join hotels on hotels.id = bookings.id_hotel\n" +
            "    where id_hotel = ?1 " +
            "    GROUP BY YEAR(date_release) , MONTH(date_release)\n" +
            "    ORDER BY YEAR(date_release) , MONTH(date_release)) AS sale", nativeQuery = true)
    ColumnChart columchart(Long idHotel);
}
