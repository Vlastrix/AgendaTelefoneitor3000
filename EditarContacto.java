import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditarContacto extends JFrame {
    private JTextField nombreField, telefonoField, correoField;
    private Contacto contactoEditar;

    public EditarContacto(Contacto contacto, AgendaTelefoneitor agenda) {
        super("Editar Contacto");
        int width = 640;
        int height = 203;

        this.contactoEditar = contacto;

        // Crear componentes de la interfaz
        JLabel nombreLabel = new JLabel("Nombre:");
        JLabel telefonoLabel = new JLabel("Teléfono:");
        JLabel correoLabel = new JLabel("Correo:");

        nombreField = new JTextField(contacto.getNombre());
        telefonoField = new JTextField(contacto.getTelefono());
        correoField = new JTextField(contacto.getCorreo());

        JButton guardarButton = new JButton("Guardar Cambios");

        // Establecer el diseño de la interfaz
        setLayout(new GridLayout(4, 2));

        // Agregar componentes a la interfaz
        add(nombreLabel);
        add(nombreField);
        add(telefonoLabel);
        add(telefonoField);
        add(correoLabel);
        add(correoField);
        add(new JLabel()); // Espacio en blanco
        add(guardarButton);

        // Agregar acción al botón de guardar
        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarCambios(agenda);
            }
        });

        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void guardarCambios(AgendaTelefoneitor agenda) {
        // Obtener los valores del formulario
        String nuevoNombre = nombreField.getText();
        String nuevoTelefono = telefonoField.getText();
        String nuevoCorreo = correoField.getText();

        contactoEditar.setNombre(nuevoNombre);
        contactoEditar.setTelefono(nuevoTelefono);
        contactoEditar.setCorreo(nuevoCorreo);

        agenda.guardarContactos();
        
        dispose();
    }
}
