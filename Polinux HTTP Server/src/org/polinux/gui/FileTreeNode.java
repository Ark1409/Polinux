package org.polinux.gui;

import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;

public class FileTreeNode extends DefaultMutableTreeNode {
	private static final long serialVersionUID = 3779049946972854659L;
	protected File absFile;
	protected boolean showFullFileName = true, showFullFillNameForSubNodes = false;;

	public FileTreeNode(String filepath) {
		this(new File(filepath));
	}

	public FileTreeNode(File file) {
		this.absFile = file.getAbsoluteFile();
	}

	public FileTreeNode(Object userObject) {
		super(userObject);
	}

	public FileTreeNode(Object userObject, boolean allowsChildren) {
		super(userObject, allowsChildren);
	}

	/**
	 * @return the showFullFileName
	 */
	public boolean isShowFullFileName() {
		return showFullFileName;
	}

	/**
	 * @param showFullFileName the showFullFileName to set
	 */
	public void setShowFullFileName(boolean showFullFileName) {
		this.showFullFileName = showFullFileName;

		if (this.showFullFileName) {
			this.setUserObject(this.absFile.getAbsolutePath());
		} else {
			this.setUserObject(this.absFile.getName());
		}
	}

	public void setup() {
		if (this.showFullFileName) {
			this.setUserObject(this.absFile.getAbsolutePath());
		} else {
			this.setUserObject(this.absFile.getName());
		}

		loadTreeNodes(this.absFile, false, this);

	}

	private DefaultMutableTreeNode loadTreeNodes(final File dir, final boolean subDir,
			final DefaultMutableTreeNode superNode) {

		if (subDir) {
			// TODO make a filter
			if (this.showFullFillNameForSubNodes) {
				superNode.setUserObject(dir.getAbsolutePath());
			} else {
				superNode.setUserObject(dir.getName());
			}
		}

		final File[] files = dir.listFiles();
		if (files != null)
			for (int i = 0; i < files.length; i++) {
				final File file = files[i];
				System.out.println("Found " + file.getAbsolutePath() + "...");
				DefaultMutableTreeNode subNode = new DefaultMutableTreeNode();

				if (this.showFullFillNameForSubNodes) {
					subNode.setUserObject(file.getAbsolutePath());
				} else {
					subNode.setUserObject(file.getName());
				}
				if (file.exists()) {
					if (file.isDirectory()) {
						loadTreeNodes(file, true, subNode);
					}
				}
				superNode.add(subNode);
			}

		return superNode;
	}

}
