package editors.quest;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.graphstream.ui.view.ViewerListener;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class QuestEditor implements ViewerListener, IDataLoaded {

	// dimensions of the window
	private static final int WIDTH = 960, HEIGHT = 500;

	private GraphView gView;
	private DataView dView;

	/**
	 * First method called in the editor
	 * 
	 * @param args - run time arguments
	 */
	public static void main(String[] args) {
		QuestEditor qe = new QuestEditor();
		qe.present();
	}

	public QuestEditor() {
		gView = new GraphView();
		gView.register(this);

		dView = new DataView();
		dView.setDataLoaded(this);
	}

	public void present() {

		// construct the window
		JFrame frame = new JFrame();
		frame.setSize(new Dimension(WIDTH, HEIGHT));

		JPanel main = new JPanel(new BorderLayout());
		main.setPreferredSize(new Dimension(WIDTH, HEIGHT));

		main.add(gView.getComponent(WIDTH / 2, HEIGHT), BorderLayout.WEST);
		main.add(dView.getComponent(WIDTH / 2, HEIGHT), BorderLayout.CENTER);

		frame.getContentPane().add(main);
		frame.pack();

		frame.setLocationRelativeTo(null);
		frame.setTitle("Quest Editor for Java Jesus");
		frame.setVisible(true);
		frame.toFront();

		frame.addWindowListener(new WindowListener() {

			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				int confirmed = JOptionPane.showConfirmDialog(null, "Are you sure you want to leave without saving?", "Exit message",
						JOptionPane.YES_NO_OPTION);

				if (confirmed == JOptionPane.YES_OPTION) {
					frame.dispose();
					System.exit(0);
				} else {
					frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				}

			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

		});

		//Node root = gView.addNode("0");
		//dView.rootLoaded(root);

	}

	@Override
	public void buttonPushed(String id) {
		dView.deselect();
		dView.select(id);

	}

	@Override
	public void buttonReleased(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void viewClosed(String id) {

	}

	private String getId(String parent, String child) {
		return parent + child + child + parent;
	}

	@Override
	public void onLoaded(JSONObject data) {

		gView.reset();
		
		if (data == null) {
			return;
		}

		JSONArray array = (JSONArray) data.get(QuestDataBuilder.QUEST_PARTS);
		
		for (int i = 0; i < array.size(); i++) {
			JSONObject obj = (JSONObject) array.get(i);
			String child = (String) obj.get(QuestDataBuilder.STATE_ID);
			dView.nodeLoaded(gView.addNode(child), obj);

			String rawParents = (String) obj.get(QuestDataBuilder.PREV_STATE_ID);
			if (rawParents != null) {
				String[] parents = rawParents.split(",");
				for (int j = 0; j < parents.length; j++) {
					String parent = parents[j].trim();

					if (!parent.isEmpty() && !gView.nodeExists(parent)) {
						
						// find the parent object
						JSONObject other = null;
						for (int k = 0; k < array.size(); k++) {
							JSONObject o = (JSONObject) array.get(k);
							String state = (String) o.get(QuestDataBuilder.STATE_ID);
							if (state != null && state.equals(parent)) {
								other = o;
								break;
							}
						}
						
						if (other == null) {
							System.err.println("Node " + parent + " doesn't exist!");
							continue;
						}
						
						dView.nodeLoaded(gView.addNode(parent), other);
					}

					if (!parent.isEmpty() && !gView.edgeExists(getId(parent, child))) {
						gView.addEdge(getId(parent, child), parent, child);
					}
				}
			}

			String rawFuture = (String) obj.get(QuestDataBuilder.FUT_STATE_ID);
			if (rawFuture != null) {
				String[] outgoing = rawFuture.split(",");
				for (int j = 0; j < outgoing.length; j++) {
					String cchild = outgoing[j].trim();

					if (!cchild.isEmpty() && !gView.nodeExists(cchild)) {
						
						// find the cchild object
						JSONObject other = null;
						for (int k = 0; k < array.size(); k++) {
							JSONObject o = (JSONObject) array.get(k);
							String state = (String) o.get(QuestDataBuilder.STATE_ID);
							if (state != null && state.equals(cchild)) {
								other = o;
								break;
							}
						}
						
						if (other == null) {
							System.err.println("Node " + cchild + " doesn't exist!");
							continue;
						}
						
						dView.nodeLoaded(gView.addNode(cchild), other);
					}

					if (!cchild.isEmpty() && !gView.edgeExists(getId(child, cchild))) {
						gView.addEdge(getId(child, cchild), child, cchild);
					}
				}
			}

		}

	}

	@Override
	public void onNodeCreated(String current, String previous, String future) {
		
		if (!gView.nodeExists(current)) {
			dView.nodeLoaded(gView.addNode(current), previous, future);
		}

		String[] parents = previous.split(",");
		for (int j = 0; j < parents.length; j++) {
			String parent = parents[j].trim();

			if (!parent.isEmpty() && !gView.nodeExists(parent)) {
				dView.nodeLoaded(gView.addNode(parent), "", current);
			}

			if (!parent.isEmpty() && !gView.edgeExists(getId(parent, current))) {
				gView.addEdge(getId(parent, current), parent, current);
			}
		}

		String[] outgoing = future.split(",");
		for (int j = 0; j < outgoing.length; j++) {
			String child = outgoing[j].trim();

			if (!child.isEmpty() && !gView.nodeExists(child)) {
				dView.nodeLoaded(gView.addNode(child), current, "");
			}

			if (!child.isEmpty() && !gView.edgeExists(getId(current, child))) {
				gView.addEdge(getId(current, child), current, child);
				dView.addIncomingEdgeToNode(child, current);
			}
		}

	}

	@Override
	public void onNodeRemoved(String current) {
		gView.removeNode(current);

	}

	@Override
	public void onNodeModified(String current, String previous, String future) {
		
		if (!gView.nodeExists(current)) {
			dView.nodeLoaded(gView.addNode(current), previous, future);
		}
		
		String[] parents = previous.split(",");
		gView.removeEnteringEdges(current);
		for (int j = 0; j < parents.length; j++) {
			String parent = parents[j].trim();

			if (!parent.isEmpty() && !gView.nodeExists(parent)) {
				dView.nodeLoaded(gView.addNode(parent), "", current);
			}
			if (!parent.isEmpty() && !gView.edgeExists(getId(parent, current))) {
				gView.addEdge(getId(parent, current), parent, current);
			}
		}

		String[] outgoing = future.split(",");
		gView.removeLeavingEdges(current);
		for (int j = 0; j < outgoing.length; j++) {
			String child = outgoing[j].trim();

			if (!child.isEmpty() && !gView.nodeExists(child)) {
				dView.nodeLoaded(gView.addNode(child), current, "");
			}
			if (!child.isEmpty() && !gView.edgeExists(getId(current, child))) {
				gView.addEdge(getId(current, child), current, child);
				dView.addIncomingEdgeToNode(child, current);
			}
		}

	}

}
