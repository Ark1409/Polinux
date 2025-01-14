package org.polinux.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class PolinuxJFrameGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1503594182977603213L;
	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public PolinuxJFrameGUI() {
		initDefaults();

		setBounds(100, 100, 799, 564);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

		tabbedPane.addTab("URIs", this.initURITable());

		contentPane.add(tabbedPane, BorderLayout.CENTER);
		contentPane.add(this.initDirTable(), BorderLayout.WEST);

	}

	private void initDefaults() {
		this.setVisible(false);
		this.setTitle("Polinux HTTP Server - 1.0.0");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setMaximumSize(Toolkit.getDefaultToolkit().getScreenSize());
		Icon icon = FileSystemView.getFileSystemView().getSystemIcon(new File("C:/"));
		UIManager.put("Tree.closedIcon", icon);
		UIManager.put("Tree.openIcon", icon);
		UIManager.put("Tree.leafIcon", icon);
		setTheme(Theme.WINDOWS);
	}

	private static final java.io.File WORKING_SPACE = new File("wwwroot").getAbsoluteFile();

	private JScrollPane initDirTable() {
		JTree tree;

		{
			// DefaultMutableTreeNode root = new
			// DefaultMutableTreeNode(WORKING_SPACE.getName());
			FileTreeNode root = new FileTreeNode(WORKING_SPACE);
			// create the child nodes
			root.setShowFullFileName(false);
			root.setup();

			// addTo(root, WORKING_SPACE);

			// create the tree by passing in the root node
			tree = new JTree(root);

			tree.setCellRenderer(new MyRenderer());

			MouseListener ml = new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					int selRow = tree.getRowForLocation(e.getX(), e.getY());
					TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
					if (selRow != -1) {
						if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
							DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
							if (node == null)
								return;
							if (!node.isLeaf())
								return;
							File f = getTreeNodeAsFile(node);
							if (f.exists()) {
								if (Desktop.isDesktopSupported()) {
									try {
										Desktop.getDesktop().open(f);
									} catch (IOException e1) {
										e1.printStackTrace();
									}
								}
							}
						}
					}
				}

				private File getTreeNodeAsFile(DefaultMutableTreeNode node) {

					final TreeNode[] nodes = node.getPath();
					String newPath = "";
					int i = 1;

					final FileTreeNode root = (FileTreeNode) node.getRoot();

					for (; i < nodes.length; i++) {
						if (i == (nodes.length - 1)) {
							newPath += nodes[i].toString();
							break;
						}
						newPath += nodes[i].toString() + "/";
					}

					File f;

					if (newPath.equalsIgnoreCase("")) {
						f = new File("./").getAbsoluteFile().getParentFile();
					} else {
						f = new File(root.absFile.getAbsolutePath() + "/" + newPath);
					}

					return f;

				}
			};

			tree.addMouseListener(ml);
		}

		JScrollPane p = new JScrollPane(tree);

		return p;
	}

	private void addTo(DefaultMutableTreeNode root, File dir) {
		final File[] files = dir.listFiles();

		for (int i = 0; i < files.length; i++) {
			final File file = files[i];

			if (file.isDirectory()) {
				DefaultMutableTreeNode node = new DefaultMutableTreeNode(file.getName());
				addTo(node, file);
				root.add(node);
			} else if (file.isFile()) {

				DefaultMutableTreeNode n = new DefaultMutableTreeNode(file.getName());

				root.add(n);
			}
		}
	}

	private JScrollPane initURITable() {
		JTable table = new JTable();
		table.setBorder(null);

		TableModel model = new DefaultTableModel(new Object[][] { { "First", "Second", "Third" } },
				new String[] { "URL", "File", "Servlet" }) {

			/**
			 * 
			 */
			private static final long serialVersionUID = -859599319708968157L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

		};
		table.setModel(model);
		JScrollPane p = new JScrollPane(table);

		return p;
	}

	public void setTheme(Theme theme) {
		try {
			UIManager.setLookAndFeel(theme.getClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		for (java.awt.Window w : java.awt.Window.getWindows()) {
			SwingUtilities.updateComponentTreeUI(w);
		}
	}

	public static enum Theme {
		METAL("javax.swing.plaf.metal.MetalLookAndFeel"), WINDOWS("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"),
		MOTIF("com.sun.java.swing.plaf.motif.MotifLookAndFeel"),
		WINDOWS_CLASSIC("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel"),
		NIMBUS("javax.swing.plaf.nimbus.NimbusLookAndFeel");

		String theme;

		Theme(String theme) {
			this.theme = theme;
		}

		public String toString() {
			return theme;
		}

		public String getClassName() {
			return theme;
		}
	}

	class MyRenderer extends DefaultTreeCellRenderer {
		private static final long serialVersionUID = 5851991690307629620L;

		private List<Object> values = new ArrayList<Object>();

		public MyRenderer() {
		}

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
				boolean leaf, int row, boolean hasFocus) {

			super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

			// if (!values.contains(value))
			setIcon(getSysIcon((DefaultMutableTreeNode) value, value));

			return this;
		}

		protected Icon getSysIcon(final DefaultMutableTreeNode node, Object add) {

			final TreeNode[] nodes = node.getPath();
			String newPath = "";
			int i = 1;

			final FileTreeNode root = (FileTreeNode) node.getRoot();

			for (; i < nodes.length; i++) {
				if (i == (nodes.length - 1)) {
					newPath += nodes[i].toString();
					break;
				}
				newPath += nodes[i].toString() + "/";
			}

			File f;

			if (newPath.equalsIgnoreCase("")) {
				f = new File("./").getAbsoluteFile().getParentFile();
			} else {
				f = new File(root.absFile.getAbsolutePath() + "/" + newPath);
			}

			System.out.println("File is2: " + f.getAbsoluteFile().getPath());

			final Icon ic = FileSystemView.getFileSystemView().getSystemIcon(f);

			values.add(add);

			return ic;

		}
	}

	static final File toRelative(File file) {
		final File cur = new File("./").getAbsoluteFile().getParentFile();
		final File abs = file.getAbsoluteFile();
		final File ret = new File(abs.getAbsolutePath().substring(cur.getAbsolutePath().length() + 1));
		return ret;
	}
}
