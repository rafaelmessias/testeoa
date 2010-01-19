package org.testeoa.dutra;

import treemap.TMComputeSize;
import treemap.TMExceptionBadTMNodeKind;
import treemap.TMNode;

//import net.bouthier.treemapSwing.TMComputeSize;
//import net.bouthier.treemapSwing.TMExceptionBadTMNodeKind;
//import net.bouthier.treemapSwing.TMNode;

public class TMSize implements TMComputeSize {

	@Override
	public float getSize(TMNode tNode) throws TMExceptionBadTMNodeKind {
		if (tNode instanceof TMTreeNode) {
			TMTreeNode tMethod = (TMTreeNode) tNode;
			// TODO: acabei de comentar aqui... vou mudar para fixo 1.0 também.
			//System.out.println(tMethod.getSize());
			return 1.0f; //tMethod.getSize();
		} else {
			return 0.0f;
		}
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
