
import com.toedter.calendar.JDateChooser;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import static java.util.Calendar.DATE;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.text.ParseException;
import java.util.GregorianCalendar;
import javax.swing.JTextField;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author SANTIAGO
 */
public class frmHospedar extends javax.swing.JInternalFrame {
    String filtro="";
    public frmHospedar() {
        initComponents();
        listarHabitaciones();
        listarHuesped();
        txtCodigo.setEditable(false);
        txtEstado.setEditable(false);
        txtNHabitacion.setEditable(false);
        txtNoche.setEditable(false);
        txtPrecio.setEditable(false);
        txtTipo.setEditable(false);
        txtTotal.setEditable(false);
        txtId.setEditable(false);
        txtNom.setEditable(false);
        btnDesocupar.setEnabled(false);
        btnImprimir.setEnabled(false);
        Calendar c1 =new GregorianCalendar();
        dateIngreso.setCalendar(c1);
        dateSalida.setCalendar(c1);
        lblAux.setVisible(false);
        lblAux.setEnabled(false);
    }
    public void listarHabitaciones(){
        DefaultTableModel modeloHabitacion = new DefaultTableModel();
        modeloHabitacion.addColumn("N. Habitacion");
        modeloHabitacion.addColumn("Tipo");
        modeloHabitacion.addColumn("Estado");
        modeloHabitacion.addColumn("Precio x Noche");
        tablaHabitaciones.setModel(modeloHabitacion);
        try{
            Coneccion cn = new Coneccion();
            Connection cnx = cn.conectar();
            modeloHabitacion = (DefaultTableModel) tablaHabitaciones.getModel();
            Statement ps;
            ResultSet rs;
            ps=cnx.createStatement();
            rs=ps.executeQuery("SELECT * FROM habitaciones");
            while(rs.next()){
                String fila [] = new String [4];
                fila[0] = rs.getString(1);
                fila[1] = rs.getString(2);
                fila[2] = rs.getString(3);
                fila[3] = rs.getString(4);

                modeloHabitacion.addRow(fila);
                tablaHabitaciones.setModel(modeloHabitacion);
            }
            
        } catch(Exception ex){
             Logger.getLogger(frmHospedar.class.getName()).log(Level.SEVERE, null, ex);
        }
        txtCodigo.requestFocus();
    }
    public void listarHuesped(){
        DefaultTableModel modeloHuesped = new DefaultTableModel();
        modeloHuesped.addColumn("Codigo");
        modeloHuesped.addColumn("Id Cliente");
        modeloHuesped.addColumn("N. Habitacion");
        modeloHuesped.addColumn("Fecha de Ingreso");
        modeloHuesped.addColumn("Fecha de Salida");
        modeloHuesped.addColumn("Cant. Noches");
        modeloHuesped.addColumn("Valor");
        tablaHuesped.setModel(modeloHuesped);
        try{
            Coneccion cn = new Coneccion();
            Connection cnx = cn.conectar();
            modeloHuesped = (DefaultTableModel) tablaHuesped.getModel();
            Statement ps;
            ResultSet rs;
            ps=cnx.createStatement();
            rs=ps.executeQuery("SELECT * FROM huespedes");
            while(rs.next()){
                String fila [] = new String [7];
                fila[0] = rs.getString(1);
                fila[1] = rs.getString(2);
                fila[2] = rs.getString(3);
                fila[3] = rs.getString(4);
                fila[4] = rs.getString(5);
                fila[5] = rs.getString(6);
                fila[6] = rs.getString(7);

                modeloHuesped.addRow(fila);
                tablaHuesped.setModel(modeloHuesped);
            }
            
        } catch(Exception ex){
             Logger.getLogger(frmHospedar.class.getName()).log(Level.SEVERE, null, ex);
        }
        txtCodigo.requestFocus();
    }
    public void calcularNoches(JDateChooser dateIngreso, JDateChooser dateSalida){
        if (dateIngreso.getDate()!=null && dateSalida.getDate()!=null) {
            Calendar fechaInicio =dateIngreso.getCalendar();
            Calendar fechafinal =dateSalida.getCalendar();
            int noche=-1;
            while(fechaInicio.before(fechafinal) || fechaInicio.equals(fechafinal)){
                noche++;
                fechaInicio.add(Calendar.DATE,1);
            }
            txtNoche.setText(""+noche);
        } else{
            JOptionPane.showMessageDialog(null, "DEBE INGRESAR UNA FECHA DE INGRESO Y OTRA DE SALIDA");
        }
    }
    public void buscardor(String valor){
            if (cbxFiltro.getSelectedItem().equals("Identificacion")) {
            filtro="Identificacion";
            }
            if (cbxFiltro.getSelectedItem().equals("Nombre")) {
                filtro="Nombre";
            }
            try {
            String [] titulos={"Identificacion","Nombre", "Apellido", "Telefono", "Correo", "Sexo", "Origen"};
            String texto= ""+valor+"_%";
            String sql = "SELECT * FROM clientes WHERE "+filtro+" LIKE "+'"'+texto+'"';
            DefaultTableModel modeloBuscar = new DefaultTableModel(null, titulos);
            Coneccion cn = new Coneccion();
            Connection cnx = cn.conectar();
            Statement pst = cnx.prepareStatement(sql);
            ResultSet rs=pst.executeQuery(sql);
            String [] fila=new String[7];
            while(rs.next()){
                fila[0]=rs.getString("Identificacion");
                fila[1]=rs.getString("Nombre");
                fila[2]=rs.getString("Apellido");
                fila[3]=rs.getString("Telefono");
                fila[4]=rs.getString("Correo");
                fila[5]=rs.getString("Sexo");
                fila[6]=rs.getString("Origen");
                modeloBuscar.addRow(fila);
            }
            tablaBusqueda.setModel(modeloBuscar);
            rs.close();
            pst.close();
        } catch (Exception e) {
                System.out.println(""+e.getMessage());
        }
            
    }
    public void calcularTotal(){
        calcularNoches(dateIngreso, dateSalida);
        double precio=Double.parseDouble(txtPrecio.getText());
        double noche=Double.parseDouble(txtNoche.getText());
        double total = (precio * noche);
        txtTotal.setText(String.valueOf(total));
    }
    public void limpiar(){
        txtCodigo.setText("");
        txtId.setText("");
        txtNom.setText("");
        txtEstado.setText("");
        txtNHabitacion.setText("");
        txtCliente.setText("");
        txtNoche.setText("");
        txtPrecio.setText("");
        txtTipo.setText("");
        txtTotal.setText("");
        Calendar c1 =new GregorianCalendar();
        dateIngreso.setCalendar(c1);
        dateSalida.setCalendar(c1);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tablaHabitaciones = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaHuesped = new javax.swing.JTable();
        txtCodigo = new javax.swing.JTextField();
        cbxFiltro = new javax.swing.JComboBox<>();
        txtNHabitacion = new javax.swing.JTextField();
        txtTipo = new javax.swing.JTextField();
        txtPrecio = new javax.swing.JTextField();
        txtEstado = new javax.swing.JTextField();
        dateIngreso = new com.toedter.calendar.JDateChooser();
        dateSalida = new com.toedter.calendar.JDateChooser();
        txtNoche = new javax.swing.JTextField();
        txtTotal = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnRegistrar = new javax.swing.JButton();
        btnDesocupar = new javax.swing.JButton();
        btnImprimir = new javax.swing.JButton();
        btnCalcular = new javax.swing.JButton();
        txtCliente = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaBusqueda = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        txtNom = new javax.swing.JTextField();
        btnModificar = new javax.swing.JButton();
        lblAux = new javax.swing.JLabel();

        setBackground(new java.awt.Color(204, 204, 204));
        setClosable(true);
        setMaximizable(true);
        setPreferredSize(new java.awt.Dimension(643, 582));

        tablaHabitaciones.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tablaHabitaciones.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaHabitacionesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaHabitaciones);

        tablaHuesped.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tablaHuesped.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaHuespedMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tablaHuesped);

        txtCodigo.setBorder(javax.swing.BorderFactory.createTitledBorder("Codigo"));

        cbxFiltro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "...", "Identificacion", "Nombre" }));
        cbxFiltro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbxFiltroMouseClicked(evt);
            }
        });

        txtNHabitacion.setBorder(javax.swing.BorderFactory.createTitledBorder("N. Habitación"));

        txtTipo.setBorder(javax.swing.BorderFactory.createTitledBorder("Tipo"));

        txtPrecio.setBorder(javax.swing.BorderFactory.createTitledBorder("Precio X Noche"));

        txtEstado.setBorder(javax.swing.BorderFactory.createTitledBorder("Estado"));

        dateIngreso.setBorder(javax.swing.BorderFactory.createTitledBorder("Fecha de Ingreso"));
        dateIngreso.setDateFormatString("yyyy-MM-dd");

        dateSalida.setBorder(javax.swing.BorderFactory.createTitledBorder("Fecha de Salida"));
        dateSalida.setDateFormatString("yyyy-MM-dd");

        txtNoche.setBorder(javax.swing.BorderFactory.createTitledBorder("Noches"));

        txtTotal.setBorder(javax.swing.BorderFactory.createTitledBorder("Total"));

        jPanel1.setBackground(new java.awt.Color(153, 153, 255));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Registro de Huespedes");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnRegistrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/editar.png"))); // NOI18N
        btnRegistrar.setText("Registrar");
        btnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarActionPerformed(evt);
            }
        });

        btnDesocupar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/icon_LogOut24.png"))); // NOI18N
        btnDesocupar.setText("Desocupar");
        btnDesocupar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDesocuparActionPerformed(evt);
            }
        });

        btnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/icon_Accounting.png"))); // NOI18N
        btnImprimir.setText("Imprimir");

        btnCalcular.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/calcular1.JPG"))); // NOI18N
        btnCalcular.setText("Calcular");
        btnCalcular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcularActionPerformed(evt);
            }
        });

        txtCliente.setBorder(javax.swing.BorderFactory.createTitledBorder("Cliente"));
        txtCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtClienteKeyReleased(evt);
            }
        });

        tablaBusqueda.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tablaBusqueda.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaBusquedaMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tablaBusqueda);

        jLabel2.setText("Filtro:");

        jPanel2.setBackground(new java.awt.Color(153, 204, 255));

        jLabel3.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Lista de Habitaciones ");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(153, 204, 255));

        jLabel4.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Lista de Huespedes");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        txtId.setBorder(javax.swing.BorderFactory.createTitledBorder("Identificación"));

        txtNom.setBorder(javax.swing.BorderFactory.createTitledBorder("Nombre"));

        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/three115.png"))); // NOI18N
        btnModificar.setText("Modificar");

        lblAux.setText("aux");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtNom, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(txtNHabitacion, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(10, 10, 10)
                            .addComponent(txtTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(txtEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(dateIngreso, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(btnCalcular, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(12, 12, 12)
                                    .addComponent(txtNoche, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(10, 10, 10)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(dateSalida, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(btnDesocupar, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAux)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addGap(2, 2, 2)
                            .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jLabel2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(cbxFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(14, 14, 14))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNHabitacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(dateSalida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(dateIngreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtNoche, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnCalcular, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnRegistrar)
                            .addComponent(btnImprimir)
                            .addComponent(btnModificar))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDesocupar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblAux))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed
        // TODO add your handling code here:
        if (txtEstado.getText().equals("Disponible")) {
            Coneccion cn = new Coneccion();
            Connection cnx = cn.conectar();
            if (!txtId.getText().equals("") && !txtNom.getText().equals("") && !txtNHabitacion.getText().equals("") && !txtTipo.getText().equals("") && !txtPrecio.getText().equals("") && !txtEstado.getText().equals("") && !txtNoche.getText().equals("") && !txtNoche.getText().equals("")) {
                try {
                    PreparedStatement pst = cnx.prepareStatement("INSERT INTO huespedes(Identificacion_Clientes, Numero_Habitaciones,"
                            + "Fecha_Ingreso, Fecha_Salida, Cant_Noches, Valor) VALUES (?,?,?,?,?,?)");
                    pst.setString(1, txtId.getText());
                    pst.setString(2, txtNHabitacion.getText());
                    pst.setString(3, ((JTextField)dateIngreso.getDateEditor().getUiComponent()).getText());
                    pst.setString(4, ((JTextField)dateSalida.getDateEditor().getUiComponent()).getText());
                    pst.setString(5, txtNoche.getText());
                    pst.setDouble(6, Double.parseDouble(txtTotal.getText()));
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "REGISTRO EXITOSO");
                    PreparedStatement st=cnx.prepareStatement("UPDATE habitaciones SET Estado = ? WHERE Numero = ?");
                    st.setString(1, "Ocupado");
                    st.setString(2, txtNHabitacion.getText());
                    st.executeUpdate();

                } catch (SQLException ex) {
                    Logger.getLogger(frmHabitacion.class.getName()).log(Level.SEVERE, null, ex);
                }  
            } else{
                JOptionPane.showMessageDialog(null, "DEBE ASEGURARSE DE SELECCIONAR UN CLIENTE Y UNA HABOTACIÓN");
            }
            
        } else{
            JOptionPane.showMessageDialog(null, "LA HABITACIÓN NO ESTA DISPONIBLE");
        }
        if (txtEstado.getText().equals("Mantenimiento")) {
            JOptionPane.showMessageDialog(null, "LA HABITACIÓN ESTA EN MANTENIMIENTO");
        }
        listarHuesped();
        listarHabitaciones();
        limpiar();
    }//GEN-LAST:event_btnRegistrarActionPerformed

    private void tablaHabitacionesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaHabitacionesMouseClicked
        // TODO add your handling code here:
        int fila = tablaHabitaciones.getSelectedRow();
        txtNHabitacion.setText((String)tablaHabitaciones.getValueAt(fila, 0));
        txtTipo.setText((String)tablaHabitaciones.getValueAt(fila, 1));
        txtEstado.setText((String)tablaHabitaciones.getValueAt(fila, 2));
        txtPrecio.setText((String)tablaHabitaciones.getValueAt(fila, 3));
    }//GEN-LAST:event_tablaHabitacionesMouseClicked

    private void btnCalcularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalcularActionPerformed
        calcularTotal();
    }//GEN-LAST:event_btnCalcularActionPerformed

    private void cbxFiltroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbxFiltroMouseClicked
    }//GEN-LAST:event_cbxFiltroMouseClicked

    private void txtClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtClienteKeyReleased
        String bs=txtCliente.getText();
        buscardor(bs);
    }//GEN-LAST:event_txtClienteKeyReleased

    private void tablaBusquedaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaBusquedaMouseClicked
        int fila = tablaBusqueda.getSelectedRow();
        txtId.setText((String)tablaBusqueda.getValueAt(fila, 0));
        txtNom.setText((String)tablaBusqueda.getValueAt(fila, 1)+" "+tablaBusqueda.getValueAt(fila, 2));
    }//GEN-LAST:event_tablaBusquedaMouseClicked

    private void tablaHuespedMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaHuespedMouseClicked
        try {
            Coneccion cn = new Coneccion();
            Connection cnx = cn.conectar();
            int fila = tablaHuesped.getSelectedRow();
            txtCodigo.setText((String)tablaHuesped.getValueAt(fila, 0));
            txtId.setText((String)tablaHuesped.getValueAt(fila, 1));
            txtNHabitacion.setText((String)tablaHuesped.getValueAt(fila, 2));
            lblAux.setText((String)tablaHuesped.getValueAt(fila, 2));
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse((String)tablaHuesped.getValueAt(fila, 3));
            dateIngreso.setDate(date1);
            Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse((String)tablaHuesped.getValueAt(fila, 4));
            dateSalida.setDate(date2);
            txtNoche.setText((String)tablaHuesped.getValueAt(fila, 5));
            txtTotal.setText((String)tablaHuesped.getValueAt(fila, 6));
            Statement st = cnx.createStatement();
            ResultSet rs=st.executeQuery("SELECT clientes.Nombre, clientes.Apellido FROM clientes WHERE Identificacion = '"+(String)tablaHuesped.getValueAt(fila, 1)+"'");
            while (rs.next()){
                txtNom.setText(rs.getString("Nombre")+" "+rs.getString("Apellido"));
            }
            Statement stm = cnx.createStatement();
            ResultSet rss=stm.executeQuery("SELECT habitaciones.Tipo, habitaciones.Estado, habitaciones.Precio FROM habitaciones WHERE Numero = '"+(String)tablaHuesped.getValueAt(fila, 2)+"'");
            while (rss.next()){
                txtTipo.setText(rss.getString("Tipo"));
                txtEstado.setText(rss.getString("Estado"));
                txtPrecio.setText(rss.getString("Precio"));
            }
            btnDesocupar.setEnabled(true);
            btnImprimir.setEnabled(true);
            btnRegistrar.setEnabled(false);
            cnx.close();
        } catch (ParseException ex) {
            Logger.getLogger(frmHospedar.class.getName()).log(Level.SEVERE, null, ex);
        } catch(Exception ex){
            Logger.getLogger(frmHospedar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_tablaHuespedMouseClicked

    private void btnDesocuparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDesocuparActionPerformed
        if (!txtCodigo.getText().equals("")) {
            Coneccion cn = new Coneccion();
            Connection cnx = cn.conectar();

            try 
            {
                PreparedStatement pst = cnx.prepareStatement("DELETE FROM huespedes WHERE Codigo = ?");
                pst.setString(1, txtCodigo.getText());
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "HABITACIÓN DESOCUPADA");
                PreparedStatement st=cnx.prepareStatement("UPDATE habitaciones SET Estado = ? WHERE Numero = ?");
                st.setString(1, "Disponible");
                st.setString(2, txtNHabitacion.getText());
                st.executeUpdate();
                btnRegistrar.setEnabled(true);
                btnImprimir.setEnabled(false);
                btnDesocupar.setEnabled(false);
            } catch (SQLException ex) {
                Logger.getLogger(frmHabitacion.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else{
            JOptionPane.showMessageDialog(null, "NO HA SELECCIONADO UN REGISTRO");
        }
        listarHabitaciones();
        listarHuesped();
        limpiar();
    }//GEN-LAST:event_btnDesocuparActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCalcular;
    private javax.swing.JButton btnDesocupar;
    private javax.swing.JButton btnImprimir;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JComboBox<String> cbxFiltro;
    private com.toedter.calendar.JDateChooser dateIngreso;
    private com.toedter.calendar.JDateChooser dateSalida;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblAux;
    private javax.swing.JTable tablaBusqueda;
    private javax.swing.JTable tablaHabitaciones;
    private javax.swing.JTable tablaHuesped;
    private javax.swing.JTextField txtCliente;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtEstado;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNHabitacion;
    private javax.swing.JTextField txtNoche;
    private javax.swing.JTextField txtNom;
    private javax.swing.JTextField txtPrecio;
    private javax.swing.JTextField txtTipo;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
}
