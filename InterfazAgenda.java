import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.net.URLEncoder;

public class InterfazAgenda extends JFrame {
    private AgendaTelefoneitor agenda;
    private JList<Contacto> listaContactos;
    private JTextArea detallesContacto;
    private JButton abrirWhatsAppButton;
    private JButton eliminarContactoButton;
    private JButton editarContactoButton;

    public InterfazAgenda() {
        int width = 640;
        int height = 203;

        setSize(width, height);

        agenda = new AgendaTelefoneitor();

        JButton importarButton = new JButton("Importar .vcf");
        importarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                importarArchivoVCF();
            }
        });

        JButton agregarContactoButton = new JButton("Agregar Contacto");
        agregarContactoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarNuevoContacto();
            }
        });

        // Tadeo
        abrirWhatsAppButton = new JButton("Abrir WhatsApp");
        abrirWhatsAppButton.setEnabled(false);
        abrirWhatsAppButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    abrirWhatsApp();
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
            }
        });

        editarContactoButton = new JButton("Editar Contacto");
        editarContactoButton.setEnabled(false);
        editarContactoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Contacto contactoSeleccionado = listaContactos.getSelectedValue();
                EditarContacto editarContactoVentana = new EditarContacto(contactoSeleccionado, agenda);
                editarContactoVentana.setVisible(true);
                actualizarListaContactos();
            }
        });


        eliminarContactoButton = new JButton("Eliminar Contacto");
        eliminarContactoButton.setEnabled(false);
        eliminarContactoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarContacto();
            }
        });

        JPanel panelLista = new JPanel(new BorderLayout());
        listaContactos = new JList<>(agenda.getContactos().toArray(new Contacto[0]));
        listaContactos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaContactos.addListSelectionListener(e -> mostrarDetallesContacto(listaContactos.getSelectedValue()));
        JScrollPane listaScrollPane = new JScrollPane(listaContactos);
        listaScrollPane.setPreferredSize(new Dimension(203, height)); // Ajuste del ancho del panel izquierdo
        panelLista.add(listaScrollPane, BorderLayout.CENTER);

        JPanel panelDetalles = new JPanel(new BorderLayout());
        detallesContacto = new JTextArea();
        detallesContacto.setEditable(false);
        JScrollPane detallesScrollPane = new JScrollPane(detallesContacto);
        panelDetalles.add(detallesScrollPane, BorderLayout.CENTER);

        JPanel subpanelBotones = new JPanel(new FlowLayout()); // Utilizamos FlowLayout para alinear los botones horizontalmente
        subpanelBotones.add(abrirWhatsAppButton);
        subpanelBotones.add(editarContactoButton);
        subpanelBotones.add(eliminarContactoButton);

        panelDetalles.add(subpanelBotones, BorderLayout.SOUTH);


        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelLista, panelDetalles);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(200);

        setTitle("Agenda Telefoneitor 3000");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setJMenuBar(createMenuBar(agregarContactoButton, importarButton));
        add(splitPane, BorderLayout.CENTER);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JMenuBar createMenuBar(JButton agregarContactoButton, JButton importarButton) {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Acciones");
        menu.add(new JMenuItem(new AbstractAction("Agregar Contacto") {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarNuevoContacto();
            }
        }));
        menu.add(new JMenuItem(new AbstractAction("Importar .vcf") {
            @Override
            public void actionPerformed(ActionEvent e) {
                importarArchivoVCF();
            }
        }));
        menuBar.add(menu);
        return menuBar;
    }

    // Tadeo
    private void mostrarDetallesContacto(Contacto contacto) {
        if (contacto != null) {
            detallesContacto.setText("Nombre: " + contacto.getNombre() + "\nTeléfono: " + contacto.getTelefono() + "\nCorreo: " + contacto.getCorreo());
            abrirWhatsAppButton.setEnabled(true);
            editarContactoButton.setEnabled(true);
            eliminarContactoButton.setEnabled(true);
        } else {
            detallesContacto.setText("");
            abrirWhatsAppButton.setEnabled(false);
            editarContactoButton.setEnabled(false);
            eliminarContactoButton.setEnabled(false);
        }
    }

    // Tadeo
    private void agregarNuevoContacto() {
        String nombre = JOptionPane.showInputDialog(this, "Ingrese el nombre del nuevo contacto:");
        String telefono = JOptionPane.showInputDialog(this, "Ingrese el teléfono del nuevo contacto:");
        String correo = JOptionPane.showInputDialog(this, "Ingrese el correo del nuevo contacto:");


        if (nombre != null && telefono != null) {
            Contacto nuevoContacto = new Contacto(nombre, telefono, correo);
            agenda.agregarContacto(nuevoContacto);
            actualizarListaContactos();
        }
    }

    private void actualizarListaContactos() {
        listaContactos.setListData(agenda.getContactos().toArray(new Contacto[0]));
    }

    // Tadeo
    private void abrirWhatsApp() throws UnsupportedEncodingException {
        Contacto contactoSeleccionado = listaContactos.getSelectedValue();
        if (contactoSeleccionado != null) {
            String textToEncode = JOptionPane.showInputDialog(this, "Mensaje para escribir al WhatsApp:");;
            String encodedText = URLEncoder.encode(textToEncode, "UTF-8");
            String numeroTelefono = contactoSeleccionado.getTelefono();
            String urlWhatsApp = "https://wa.me/" + numeroTelefono + "?text=" + encodedText;

            try {
                Desktop.getDesktop().browse(new URI(urlWhatsApp));
            } catch (IOException | URISyntaxException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al abrir WhatsApp en el navegador.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void importarArchivoVCF() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory() || file.getName().toLowerCase().endsWith(".vcf");
            }

            @Override
            public String getDescription() {
                return "Archivos VCF (*.vcf)";
            }
        });

        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivoVCF = fileChooser.getSelectedFile();
            if (archivoVCF != null) {
                List<Contacto> contactosImportados = leerArchivoVCF(archivoVCF);
                if (!contactosImportados.isEmpty()) {
                    for (Contacto contacto : contactosImportados) {
                        agenda.agregarContacto(contacto);
                    }
                    actualizarListaContactos();
                    JOptionPane.showMessageDialog(this, "Se han importado " + contactosImportados.size() + " contactos.", "Importación exitosa", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "El archivo VCF no contiene contactos válidos.", "Error de importación", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    // Tadeo
    private List<Contacto> leerArchivoVCF(File archivoVCF) {
        List<Contacto> contactos = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(archivoVCF))) {
            String linea;
            String nombre = null;
            String telefono = null;
            String correo = null;

            while ((linea = br.readLine()) != null) {
                if (linea.startsWith("EMAIL:")) {
                    correo = linea.substring(6);
                } else if (linea.startsWith("FN:")) {
                    nombre = linea.substring(3);
                } else if (linea.startsWith("TEL:")) {
                    telefono = linea.substring(4);
                    if (nombre != null && telefono != null) {
                        contactos.add(new Contacto(nombre, telefono, correo));
                        nombre = null;
                        telefono = null;
                        correo = null;
                    }   
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return contactos;
    }
    // Tadeo
    private void eliminarContacto() {
        Contacto contactoSeleccionado = listaContactos.getSelectedValue();
        if (contactoSeleccionado != null) {
            int confirmacion = JOptionPane.showConfirmDialog(
                    this,
                    "¿Estás seguro de que deseas eliminar este contacto?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                agenda.getContactos().remove(contactoSeleccionado);
                actualizarListaContactos();
                agenda.guardarContactos();
                abrirWhatsAppButton.setEnabled(false);
                eliminarContactoButton.setEnabled(false);
            }
        }
    }
}
