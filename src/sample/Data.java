package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class Data {


    private Connection conn;
    private PreparedStatement st;

    private ObservableList<Movie> movies;
    private ObservableList<Session> sessions;
    private ObservableList<TicketPrice> ticketPrices;
    private ObservableList<Ticket> tickets;
    private ObservableList<Integer> seatsRow;

    public ObservableList<String> getReserveSeat;

    public int selectedMovieId;
    public int selectedMovieNumber;
    public String movieTitle;
    public String email;
    public String selectedDate;
    public String selectedTime;
    public int ticketPrice;
    public int selectedHallId;

    private static Data instance = new Data();

    private final String query_movies = "SELECT * FROM movies";
    private final String query_sessions = "SELECT * FROM SESSIONS";
    private final String query_seats = "SELECT seats.id, hall_id, row_num, seat_number, seats FROM row_seats INNER JOIN seats ON row_seats.id = row_id";
    private final String query_ticket_price = "SELECT * FROM ticket_price";
    private final String query_update_seat_status = "UPDATE seats SET status = false WHERE id = ?";
    private final String query_booking = "SELECT seats.id, ses.id, row_num, seat_number, status, ses.movie_id, ses.date, ses.time FROM (row_seats INNER JOIN seats ON row_seats.id = row_id) INNER JOIN hr.sessions ses ON ses.hall_id = row_seats.hall_id";
    private final String insert_ticket = "INSERT INTO tickets (session_id, customer_id, seat_id) VALUES (?, ?, ?)";
    private final String get_ticket_info = "SELECT session_id, seat_id FROM tickets";
    private final String get_rows = "SELECT MAX(row_num) FROM row_seats WHERE hall_id = ?";
    private final String insert_customer = "INSERT INTO customers (first_name, last_name, email, phone_number, password) VALUES (?, ?, ?, ?, ?)";
    private final String query_tickets = "SELECT t.id, t.session_id, ses.movie_id, ses.hall_id, t.seat_id, rs.row_num, s.seat_number, ses.date, ses.time " +
            "FROM tickets t INNER JOIN sessions ses ON t.session_id = ses.id " +
            "INNER JOIN seats s ON t.seat_id = s.id INNER JOIN row_seats rs ON s.row_id = rs.id";


    private final String get_hall_capacity = "SELECT COUNT(*) FROM seats s INNER JOIN row_seats rs ON s.row_id = rs.id WHERE rs.hall_id = ?";


    private PreparedStatement updateSeatStatus;

    public static Data getInstance(){
        return instance;
    }


    public void insertTicketInfo(int sessionId, int customerId, int seatId){
        initializeDatabase();
        try {
            PreparedStatement st = conn.prepareStatement(insert_ticket);
            st.setInt(1, sessionId);
            st.setInt(2, customerId);
            st.setInt(3, seatId);

            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getTicketInfo(int column, int row) {
        initializeDatabase();
        try(Statement statement = conn.createStatement()){
            ResultSet rs = statement.executeQuery(get_ticket_info);
            int id = 0;
            for (int i = 0; i < row; i++){
                rs.next();
                id = rs.getInt(column);
            }
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getCountOfTickets(){
        try(Statement statement = conn.createStatement()){
            ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM tickets");
            rs.next();
            return rs.getInt(1);
        }catch (SQLException e){
            System.out.println("Error from update: " + e.getMessage());
            return 0;
        }
    }

    public int getHallCapacity(int hallId){
        try {
            PreparedStatement st = conn.prepareStatement(get_hall_capacity);
            st.setInt(1, hallId);
            ResultSet rs = st.executeQuery();
            rs.next();
            return rs.getInt(1);
        }catch (SQLException e){
            System.out.println("Вместимость: " + e.getMessage());
            return 0;
        }
    }

    public int getRows(int hallId){
        try {
            PreparedStatement st = conn.prepareStatement(get_rows);
            st.setInt(1, hallId);
            ResultSet rs = st.executeQuery();
            rs.next();
            return rs.getInt(1);
        }catch (SQLException e){
            System.out.println("Вместимость: " + e.getMessage());
            return 0;
        }
    }

    public ObservableList<Movie> listMovies(){
        initializeDatabase();
        try(Statement statement = conn.createStatement()){
            ResultSet rs = statement.executeQuery(query_movies);
            movies = FXCollections.observableArrayList();
            while (rs.next()){
                int id = rs.getInt(1);
                String title = rs.getString(2);
                int year = rs.getInt(3);
                String director = rs.getString(4);
                String duration = rs.getString(6);
                String genre = rs.getString(5);
                int ageLimit = rs.getInt(7);
                String description = rs.getString(8);
                String url = rs.getString(9);
                Movie newMovie = new Movie(title, year, director, duration, genre, ageLimit, description, url);
                newMovie.setId(id);
                movies.add(newMovie);
            }
            return movies;
        }catch (SQLException e){
            System.out.println("Error from listMovies(): " + e.getMessage());
            return null;
        }
    }

    public ObservableList<Session> listSessions(){
        initializeDatabase();
        try(Statement statement = conn.createStatement()){
            ResultSet rs = statement.executeQuery(query_sessions);
            sessions = FXCollections.observableArrayList();
            while (rs.next()){
                int id = rs.getInt(1);
                int hallId = rs.getInt(2);
                int movieId = rs.getInt(3);
                String date = rs.getString(4);
                String time = rs.getString(5);
                int price = rs.getInt(6);
                Session newSession = new Session(hallId, movieId, date, time, price);
                newSession.setId(id);
                sessions.add(newSession);
            }
            return sessions;
        }catch (SQLException e){
            System.out.println("Error from listSessions(): " + e.getMessage());
            return null;
        }
    }

    public ObservableList<Ticket> listTickets(){
        initializeDatabase();
        try(Statement statement = conn.createStatement()){
            ResultSet rs = statement.executeQuery(query_tickets);
            tickets = FXCollections.observableArrayList();
            while (rs.next()){
                int id = rs.getInt(1);
                int sessionId = rs.getInt(2);
                int movieId = rs.getInt(3);
                int hallId = rs.getInt(4);
                int seatId = rs.getInt(5);
                int rowNumber = rs.getInt(6);
                int seatNumber = rs.getInt(7);
                String date = rs.getString(8);
                String time = rs.getString(9);
                Ticket ticket = new Ticket(sessionId, movieId, hallId, seatId, rowNumber, seatNumber, date, time);
//                ticket.setId(id);
                tickets.add(ticket);
            }
            return tickets;
        }catch (SQLException e){
            System.out.println("Error from listTickets(): " + e.getMessage());
            return null;
        }
    }

    public void selectedDate(String date){
        selectedDate = date;
    }

    public void selectedTime(String time){
        selectedTime = time;
    }

    public void getTicketPrice(int price){
        ticketPrice = price;
    }

    public void selectedHall(int hallID){
        selectedHallId = hallID;
    }

    public int getSeatsInfo(String column, int seatId){
        initializeDatabase();
        String query = "SELECT " + column + " FROM seats s INNER JOIN row_seats rs ON s.row_id = rs.id " +
                            "WHERE s.id = " + seatId;
        try(Statement statement = conn.createStatement()){
            ResultSet rs = statement.executeQuery(query);
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e){
            e.printStackTrace();
            return 0;
        }
    }

    public int getCustomerId(){
        initializeDatabase();
        String emailD = "SELECT id FROM customers WHERE email = '" + email + "'";
        try(Statement statement = conn.createStatement()){
            ResultSet rs = statement.executeQuery(emailD);
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e){
            e.printStackTrace();
            return 0;
        }
    }

    public boolean validateLogin(String emailTextField, String passwordField){
        initializeDatabase();
        String verifyLogin = "SELECT COUNT(1) FROM customers WHERE email = '" + emailTextField + "' AND password = '" + passwordField + "'";
        boolean flag = false;
        try(Statement statement = conn.createStatement()){
            ResultSet rs = statement.executeQuery(verifyLogin);

            while (rs.next()){
                if (rs.getInt(1) == 1) {
                    flag = true;
                }
                else
                    flag = false;
            }

        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return flag;
    }

    public boolean insertCustomers(String firstName, String lastName, String email, Long phoneNumber, String password){
        initializeDatabase();
        try {
            PreparedStatement st = conn.prepareStatement(insert_customer);
            st.setString(1, firstName);
            st.setString(2, lastName);
            st.setString(3, email);
            st.setLong(4, phoneNumber);
            st.setString(5, password);

            st.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public void selectedMovieId(int selectedMovie){
        selectedMovieId = selectedMovie;
    }

    public void selectedMovieNumber(int selectedMovie){
        selectedMovieNumber = selectedMovie;
    }

    public void selectedMovieTitle(String selectedMovie){
        movieTitle = selectedMovie;
    }

    public void customerEmail(String emailCustomer){
        email = emailCustomer;
    }

    public void getReserveSeats(ObservableList<String> reserveSeats){
        getReserveSeat = FXCollections.observableArrayList();
        getReserveSeat = reserveSeats;
    }

    public void initializeDatabase() {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/hr?&serverTimezone=UTC";
        String user = "root";
        String password = "root";
        try {
            Class.forName(driver);
            String query = "SELECT * FROM movie;";
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
