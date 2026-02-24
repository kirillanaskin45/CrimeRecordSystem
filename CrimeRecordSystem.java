package crimerecordsystem;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.border.TitledBorder;

class CrimeRecord {
    String id, crimeType, location, accusedName, officer, date;
    
    CrimeRecord(String id, String crimeType, String location, String accusedName, String officer, String date) {
        this.id = id;
        this.crimeType = crimeType;
        this.location = location;
        this.accusedName = accusedName;
        this.officer = officer;
        this.date = date;
    }
}

public class CrimeRecordSystem extends JFrame {
    
    private JTextField tfId, tfLocation, tfAccused, tfOfficer, tfSearch, tfDate;
    private JComboBox<String> cbCrimeType;
    private DefaultTableModel tableModel;
    private JTable table;
    private ArrayList<CrimeRecord> crimeList = new ArrayList<>();
    private JLabel statusLabel;
    
    public CrimeRecordSystem() {
        setTitle("Crime Record Management System");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Устанавливаем современный look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Верхняя панель с заголовком
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(25, 25, 112));
        topPanel.setPreferredSize(new Dimension(getWidth(), 70));
        
        JLabel title = new JLabel("CRIME RECORD MANAGEMENT SYSTEM");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        topPanel.add(title);
        add(topPanel, BorderLayout.NORTH);
        
        // Основная панель с формой и таблицей
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(350);
        splitPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Левая панель - форма ввода
        JPanel formPanel = createFormPanel();
        splitPane.setLeftComponent(formPanel);
        
        // Правая панель - таблица
        JPanel tablePanel = createTablePanel();
        splitPane.setRightComponent(tablePanel);
        
        add(splitPane, BorderLayout.CENTER);
        
        // Нижняя панель - поиск и статистика
        add(createBottomPanel(), BorderLayout.SOUTH);
        
        // Добавляем тестовые данные
        addSampleData();
        showAllRecords();
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 248, 255));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
            "Добавить запись",
            TitledBorder.CENTER,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14),
            new Color(25, 25, 112)
        ));
        
        JPanel formGrid = new JPanel(new GridBagLayout());
        formGrid.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        
        // Поля ввода
        tfId = createStyledTextField();
        cbCrimeType = new JComboBox<>(new String[]{
            " Murder", "Theft", "Fraud", "Assault", 
            "Cybercrime", "Robbery", "Arson", "Drug Trafficking"
        });
        tfLocation = createStyledTextField();
        tfAccused = createStyledTextField();
        tfOfficer = createStyledTextField();
        tfDate = createStyledTextField();
        tfDate.setText("2024-01-01");
        
        // Добавляем компоненты
        gbc.gridy = 0;
        addFormField(formGrid, gbc, "ID преступления:", tfId);
        gbc.gridy = 1;
        addFormField(formGrid, gbc, "Тип преступления:", cbCrimeType);
        gbc.gridy = 2;
        addFormField(formGrid, gbc, "Место:", tfLocation);
        gbc.gridy = 3;
        addFormField(formGrid, gbc, "Обвиняемый:", tfAccused);
        gbc.gridy = 4;
        addFormField(formGrid, gbc, "Следователь:", tfOfficer);
        gbc.gridy = 5;
        addFormField(formGrid, gbc, "Дата:", tfDate);
        
        // Кнопки
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        buttonPanel.setBackground(new Color(240, 248, 255));
        
        JButton btnAdd = createStyledButton("Добавить запись", new Color(46, 204, 113));
        JButton btnClear = createStyledButton("Очистить форму", new Color(52, 152, 219));
        
        btnAdd.addActionListener(e -> addRecord());
        btnClear.addActionListener(e -> clearForm());
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnClear);
        
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        formGrid.add(buttonPanel, gbc);
        
        panel.add(formGrid, BorderLayout.NORTH);
        
        return panel;
    }
    
    private void addFormField(JPanel panel, GridBagConstraints gbc, String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(jLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(field, gbc);
    }
    
    private JTextField createStyledTextField() {
        JTextField field = new JTextField(15);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return field;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(bgColor.brighter());
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bgColor);
            }
        });
        
        return btn;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
            "Список преступлений",
            TitledBorder.CENTER,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14),
            new Color(25, 25, 112)
        ));
        
        // Создаем таблицу
        tableModel = new DefaultTableModel(
            new String[]{"ID", "Тип", "Место", "Обвиняемый", "Следователь", "Дата"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(70, 130, 180));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(135, 206, 250));
        
        // Чередование цветов строк
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 248, 255));
                }
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Панель управления таблицей
        JPanel tableControl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        tableControl.setBackground(Color.WHITE);
        
        JButton btnDelete = createStyledButton("️Удалить", new Color(231, 76, 60));
        JButton btnEdit = createStyledButton("Редактировать", new Color(241, 196, 15));
        
        btnDelete.addActionListener(e -> deleteRecord());
        btnEdit.addActionListener(e -> editRecord());
        
        tableControl.add(btnEdit);
        tableControl.add(btnDelete);
        
        panel.add(tableControl, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Панель поиска
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(new Color(245, 245, 245));
        
        searchPanel.add(new JLabel("Поиск по типу преступления:"));
        tfSearch = createStyledTextField();
        tfSearch.setPreferredSize(new Dimension(150, 25));
        searchPanel.add(tfSearch);
        
        JButton btnSearch = createStyledButton("Найти", new Color(155, 89, 182));
        JButton btnShowAll = createStyledButton("Показать все", new Color(52, 152, 219));
        
        btnSearch.addActionListener(e -> searchCrimeType());
        btnShowAll.addActionListener(e -> showAllRecords());
        
        searchPanel.add(btnSearch);
        searchPanel.add(btnShowAll);
        
        // Статус панель
        statusLabel = new JLabel("Всего записей: 0");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        statusLabel.setForeground(new Color(46, 204, 113));
        
        panel.add(searchPanel, BorderLayout.WEST);
        panel.add(statusLabel, BorderLayout.EAST);
        
        return panel;
    }
    
    private void addRecord() {
        String id = tfId.getText().trim();
        String crimeType = cbCrimeType.getSelectedItem().toString();
        String location = tfLocation.getText().trim();
        String accused = tfAccused.getText().trim();
        String officer = tfOfficer.getText().trim();
        String date = tfDate.getText().trim();
        
        if (id.isEmpty() || location.isEmpty() || accused.isEmpty() || officer.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Пожалуйста, заполните все поля!", 
                "Ошибка", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Проверка на уникальность ID
        for (CrimeRecord cr : crimeList) {
            if (cr.id.equals(id)) {
                JOptionPane.showMessageDialog(this, 
                    "ID должен быть уникальным!", 
                    "Ошибка", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        crimeList.add(new CrimeRecord(id, crimeType, location, accused, officer, date));
        showAllRecords();
        clearForm();
        
        JOptionPane.showMessageDialog(this, 
            "Запись успешно добавлена!", 
            "Успех", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void clearForm() {
        tfId.setText("");
        tfLocation.setText("");
        tfAccused.setText("");
        tfOfficer.setText("");
        tfDate.setText("2024-01-01");
        cbCrimeType.setSelectedIndex(0);
    }
    
    private void showAllRecords() {
        tableModel.setRowCount(0);
        for (CrimeRecord cr : crimeList) {
            tableModel.addRow(new Object[]{
                cr.id, 
                cr.crimeType, 
                cr.location, 
                cr.accusedName, 
                cr.officer,
                cr.date
            });
        }
        updateStatus();
    }
    
    private void searchCrimeType() {
        String search = tfSearch.getText().trim().toLowerCase();
        tableModel.setRowCount(0);
        
        if (search.isEmpty()) {
            showAllRecords();
            return;
        }
        
        for (CrimeRecord cr : crimeList) {
            if (cr.crimeType.toLowerCase().contains(search)) {
                tableModel.addRow(new Object[]{
                    cr.id, cr.crimeType, cr.location, 
                    cr.accusedName, cr.officer, cr.date
                });
            }
        }
        updateStatus();
    }
    
    private void deleteRecord() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, 
                "Выберите запись для удаления!", 
                "Ошибка", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Вы уверены, что хотите удалить эту запись?",
            "Подтверждение",
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            String id = tableModel.getValueAt(row, 0).toString();
            crimeList.removeIf(cr -> cr.id.equals(id));
            showAllRecords();
            
            JOptionPane.showMessageDialog(this,
                "Запись успешно удалена!",
                "Успех",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void editRecord() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, 
                "Выберите запись для редактирования!", 
                "Ошибка", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Заполняем форму данными выбранной записи
        tfId.setText(tableModel.getValueAt(row, 0).toString());
        cbCrimeType.setSelectedItem(tableModel.getValueAt(row, 1).toString());
        tfLocation.setText(tableModel.getValueAt(row, 2).toString());
        tfAccused.setText(tableModel.getValueAt(row, 3).toString());
        tfOfficer.setText(tableModel.getValueAt(row, 4).toString());
        tfDate.setText(tableModel.getValueAt(row, 5).toString());
        
        // Удаляем старую запись
        String id = tableModel.getValueAt(row, 0).toString();
        crimeList.removeIf(cr -> cr.id.equals(id));
    }
    
    private void updateStatus() {
        statusLabel.setText("�Всего записей: " + tableModel.getRowCount());
    }
    
    private void addSampleData() {
        crimeList.add(new CrimeRecord("001", "Murder", "New York", "John Smith", "Det. Johnson", "2024-01-15"));
        crimeList.add(new CrimeRecord("002", "Theft", "Los Angeles", "Mike Brown", "Sgt. Williams", "2024-01-16"));
        crimeList.add(new CrimeRecord("003", "Cybercrime", "Chicago", "Alex Wilson", "Special Agent Davis", "2024-01-17"));
        crimeList.add(new CrimeRecord("004", "Fraud", "Miami", "Sarah Lee", "Det. Martinez", "2024-01-18"));
        crimeList.add(new CrimeRecord("005", "Assault", "Houston", "Tom Harris", "Officer Thompson", "2024-01-19"));
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new CrimeRecordSystem().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
