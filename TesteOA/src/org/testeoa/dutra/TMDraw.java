package org.testeoa.dutra;

import java.awt.Color;
import java.awt.Paint;

import treemap.TMComputeDraw;
import treemap.TMExceptionBadTMNodeKind;
import treemap.TMNode;
import treemap.TMNodeAdapter;

//import net.bouthier.treemapSwing.TMComputeDraw;
//import net.bouthier.treemapSwing.TMExceptionBadTMNodeKind;
//import net.bouthier.treemapSwing.TMNode;
//import net.bouthier.treemapSwing.TMNodeAdapter;

public class TMDraw implements TMComputeDraw {

	@Override
	public Paint getFilling(TMNodeAdapter tAdapter) throws TMExceptionBadTMNodeKind {
		if (tAdapter.getNode() instanceof TMTreeNode) {
			TMTreeNode tNode = (TMTreeNode) tAdapter.getNode();
			if (tNode.isMetodo) {
				return Color.RED;
			} else {
				return Color.BLUE;
			}
		} else if (tAdapter.getNode() instanceof TMTreeClass) {
			return Color.GREEN;
		} else if (tAdapter.getNode() instanceof TMTreePackage) {
			return Color.ORANGE;
		} else if (tAdapter.getNode() instanceof TMTreeRoot) {
			return Color.YELLOW;
		}
		return Color.WHITE;
	}

	@Override
	public String getTitle(TMNodeAdapter tAdapter) throws TMExceptionBadTMNodeKind {
		if (tAdapter.getNode() instanceof TMTreeNode) {
			TMTreeNode tNode = (TMTreeNode) tAdapter.getNode();
			return tNode.getNome();
		} else if (tAdapter.getNode() instanceof TMTreeClass) {
			TMTreeClass tClass = (TMTreeClass) tAdapter.getNode();
			return tClass.getDescription();
		} else if (tAdapter.getNode() instanceof TMTreePackage) {
			TMTreePackage tPackage = (TMTreePackage) tAdapter.getNode();
			return tPackage.getDescription();
		} else if (tAdapter.getNode() instanceof TMTreeRoot) {
			TMTreeRoot tRoot = (TMTreeRoot) tAdapter.getNode();
			return tRoot.getNome();
		} else {
			return "***";
		}
	}

	@Override
	public Paint getTitleColor(TMNodeAdapter arg0)
			throws TMExceptionBadTMNodeKind {
		// TODO Auto-generated method stub
		return Color.BLACK;
	}

	@Override
	public String getTooltip(TMNodeAdapter arg0)
			throws TMExceptionBadTMNodeKind {
		// TODO Auto-generated method stub
		return "*";
	}

	@Override
	public boolean isCompatibleWith(TMNode tNode) {
		if ((tNode instanceof TMTreeNode) || (tNode instanceof TMTreeClass)
				|| (tNode instanceof TMTreePackage)
				|| (tNode instanceof TMTreeRoot)) {
			return true;
		}
		return false;
	}

}
