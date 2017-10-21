package graphtea.extensions.reports.planarity.planaritypq;

import java.util.*;
import java.util.stream.Collectors;

import static graphtea.extensions.reports.planarity.planaritypq.PQHelpers.*;

public class PQ {

    public PQNode bubble(PQNode _root, List<PQNode> S){
        Queue<PQNode> queue = new LinkedList<>(S);
        int blockCount = 0;
        int blockedNodes = 0;
        int offTheTop = 0;

        while(queue.size() + blockCount + offTheTop > 1){
            //System.out.println("|Queue| = " + queue.size());
            if(queue.size() == 0){
                _root = null;
                return _root;
            }

            PQNode x = queue.remove();
            x.blocked = true;
            //System.out.println("Processing: " + x.id);

            List<PQNode> BS = new ArrayList<>();
            List<PQNode> US = new ArrayList<>();
            for(PQNode u : x.immediateSiblings()){
                if(u == null){
                    continue;
                }
                if(u.blocked){
                    BS.add(u);
                }
                else {
                    US.add(u);
                }
            }
            //System.out.println("|BS| = " + BS.size());
            //System.out.println("|US| = " + US.size());
            if(US.size() > 0){
                PQNode y = US.get(0);
                x.parent = y.parent;
                x.blocked = false;
            }
            else if(x.immediateSiblings().size() < 2){
                x.blocked = false;
            }

            int listSize = 0;
            if(!x.blocked){
                PQNode y = x.parent;
                if(BS.size() > 0){
                    Set<PQNode> list = x.maximalConsecutiveSetOfSiblingsAdjacent(true);

                    listSize = list.size();
                    for(PQNode z : list){
                        z.blocked = false;
                        y.pertinentChildCount++;
                    }
                }
                if(y == null){
                    offTheTop = 1;
                }
                else {
                    y.pertinentChildCount++;
                    if(!y.queued){
                        queue.add(y);
                        y.queued = true;
                    }
                }
                blockCount = blockCount - BS.size();
                blockedNodes = blockedNodes - listSize;
            }
            else {
                blockCount = blockCount + 1 - BS.size();
                blockedNodes = blockedNodes + 1;
            }

        }

        return _root;
    }

    public PQNode reduce(PQNode T, List<PQNode> S){
        Queue<PQNode> queue = new LinkedList<>(S);
        for(PQNode x : S){
            x.pertinentLeafCount = 1;
        }
        while(queue.size() > 0){
            PQNode x = queue.remove();
            if(x.pertinentLeafCount < S.size()){
                // X is not ROOT(T, S)

                PQNode y = x.parent;
                y.pertinentLeafCount = y.pertinentLeafCount + x.pertinentLeafCount;
                y.pertinentChildCount = y.pertinentChildCount - 1;
                if(y.pertinentChildCount == 0){
                    queue.add(y);
                }
                //if(TEMPLATE_L1(x)) break;
                if(TEMPLATE_P1(x)) break;
                //if(TEMPLATE_P3(x)) break;
                //if(TEMPLATE_P5(x)) break;
                //if(TEMPLATE_Q1(x)) break;
                //if(TEMPLATE_Q2(x)) break;

                // If all templates fail, return null tree
                return null;
            }
            else {
                // X is ROOT(T, S)

                //if(TEMPLATE_L1(x)) break;
                if(TEMPLATE_P1(x)) break;
                //if(TEMPLATE_P2(x)) break;
                //if(TEMPLATE_P4(x)) break;
                //if(TEMPLATE_P6(x)) break;
                //if(TEMPLATE_Q1(x)) break;
                //if(TEMPLATE_Q2(x)) break;
                //if(TEMPLATE_Q3(x)) break;

                // If all templates fail, return null tree
                return null;
            }

        }

        return T;
    }

    public void replace(PQNode T, PQNode TPrime){

    }

    public PQNode root(PQNode T, List<PQNode> S){

        return null;
    }

    /**
    * TEMPLATES
    * */

    public boolean TEMPLATE_L1(PQNode x){ return false; }

    public boolean GENERALIZED_TEMPLATE_1(PQNode x){
        if(!x.labelType.equals(PQNode.FULL)){
            for(PQNode n : x.children){
                if(!n.labelType.equals(PQNode.FULL)){
                    return false;
                }
            }
            x.labelType = PQNode.FULL;
            return true;
        }
        return false;
    }

    public boolean TEMPLATE_P1(PQNode x){
       if (x.nodeType == PQNode.PNODE) {
           return GENERALIZED_TEMPLATE_1(x);
       }
       return false;
    }

    public boolean TEMPLATE_P2(PQNode x) {

        //Matching Phase

        //If not root
        if (x.parent != null) {
            return false;
        }

        List<PQNode> emptyChildren = new ArrayList<PQNode>();
        List<PQNode> fullChildren = new ArrayList<PQNode>();

        for (PQNode child : x.getChildren()) {
            if (child.labelType == PQNode.FULL) {
                fullChildren.add(child);
            }
            else if (child.labelType == PQNode.EMPTY) {
                emptyChildren.add(child);
            }
        }

        //If there are no full nodes
        if (fullChildren.size() == 0) {
            return false;
        }
        //If there are no empty nodes
        if (emptyChildren.size() == 0) {
            return false;
        }
        //If there were other nodes than full or empty
        if ( fullChildren.size() + emptyChildren.size() != x.children.size()) {
            return false;
        }


        //Replacement phase

        PQNode fullParent = new PQNode();
        fullParent.nodeType = PQNode.PNODE;
        fullParent.labelType = PQNode.FULL;
        fullParent.parent = x;

        //Adding the full children to a new P node
        fullParent.children = fullChildren;

        //Pointing the children to the new P node
        for (PQNode child : fullChildren) {
            child.parent = fullParent;
        }

        //Setting the links again, otherwise the endmost children would point to the previous siblings (the empty ones)
        setCircularLinks(fullChildren);

        return true;
    }

    /**
     * This case is very similiar to TEMPLATE_P2.
     * The matching is nearly identical, the only different being that x cannot be a root.
     */
    public boolean TEMPLATE_P3(PQNode x){

        //Matching Phase

        //If root
        if (x.parent == null) {
            return false;
        }

        List<PQNode> emptyChildren = new ArrayList<PQNode>();
        List<PQNode> fullChildren = new ArrayList<PQNode>();

        for (PQNode child : x.getChildren()) {
            if (child.labelType == PQNode.FULL) {
                fullChildren.add(child);
            }
            else if (child.labelType == PQNode.EMPTY) {
                emptyChildren.add(child);
            }
        }

        //If there are no full nodes
        if (fullChildren.size() == 0) {
            return false;
        }
        //If there are no empty nodes
        if (emptyChildren.size() == 0) {
            return false;
        }
        //If there were other nodes than full or empty
        if ( fullChildren.size() + emptyChildren.size() != x.children.size()) {
            return false;
        }

        //Replacement phase

        x.labelType = PQNode.PARTIAL;
        x.nodeType = PQNode.QNODE;
        x.children = new ArrayList<PQNode>();

        PQNode emptyPNode = new PQNode();
        PQNode fullPNode = new PQNode();

        x.children.add(emptyPNode);
        x.children.add(fullPNode);

        emptyPNode.nodeType = PQNode.PNODE;
        fullPNode.nodeType = PQNode.PNODE;
        emptyPNode.labelType = PQNode.EMPTY;
        fullPNode.labelType = PQNode.FULL;

        emptyPNode.parent = x;
        fullPNode.parent = x;

        //Adding the children to the appropriate P node
        emptyPNode.children = emptyChildren;
        fullPNode.children = fullChildren;


        //Pointing the children to the appropriate P node
        for (PQNode child : emptyChildren) {
            child.parent = emptyPNode;
        }
        for (PQNode child : fullChildren) {
            child.parent = fullPNode;
        }

        //Setting the links again, otherwise the endmost children would point to the previous siblings (the empty ones)
        setCircularLinks(emptyChildren);
        setCircularLinks(fullChildren);

        return true;
    }

    public boolean TEMPLATE_P4(PQNode x){
        if(!x.labelType.equals(PQNode.PARTIAL)){
            return false;
        }

        PQNode pNode = new PQNode();
        pNode.nodeType = PQNode.PNODE;
        pNode.labelType = PQNode.FULL;
        List<PQNode> emptyChildren = new ArrayList<PQNode>();
        List<PQNode> fullChildren = new ArrayList<PQNode>();
        int numPartialQNodes = 0;
        PQNode partialQNode = null;
        for(PQNode n : x.children){
            if(n.labelType.equals(PQNode.PARTIAL) && n.nodeType.equals(PQNode.QNODE)){
                partialQNode = n;
                numPartialQNodes++;
                emptyChildren.add(n);
            }
            else if(n.labelType.equals(PQNode.FULL)){
                fullChildren.add(n);
                n.parent = pNode;
            }
            else {
                emptyChildren.add(n);
            }

        }

        if(numPartialQNodes != 1){
            return false;
        }

        pNode.children = fullChildren;
        pNode.parent = partialQNode;
        partialQNode.children.add(pNode);

        setCircularLinks(fullChildren);
        setCircularLinks(emptyChildren);
        setCircularLinks(partialQNode.children);

        return true;
    }

    public boolean TEMPLATE_P5(PQNode x){

        if(x.nodeType(PQNode.PNODE)){
            return false;
        }
        if(x.partialChildren().size() != 1){
            return false;
        }

        // TODO: Y := the unique element in PARTIAL_CHILDREN(X)
        PQNode y = x.partialChildren().get(0);
        // TODO: EC := the unique element in ENDMOST_CHILDREN(Y) labeled "empty"
        PQNode ec = x.endmostChildren().get(0);
        // TODO: FC := the unique element in ENDMOST_CHILDREN(Y) labeled "full"
        PQNode fc = x.endmostChildren().get(0);

        // The following statement may be performed in time on the order of the number of
        // pertinent children of X through the use of the CIRCULAR_LINK fields

        PQNode es = null;
        if(y.emptySibling() != null){
            es = y.emptySibling();
        }

        // Y will be the root of the replacement

        y.parent = x.parent;
        y.pertinentLeafCount = x.pertinentLeafCount;
        y.labelType = PQNode.PARTIAL;

        // Remove Y from the list of children of X formed by the CIRCULAR_LINK fields
        y.removeFromCircularLink();

        if(x.immediateSiblings().size() == 0){
            x.parent.replaceInCircularLink(x);
        }
        else {
            x.parent.replaceInImmediateSiblings(x, y);
            if(y.parent.endmostChildren().contains(x)) {
                y.parent.endmostChildren().removeAll(union(Arrays.asList(x), Arrays.asList(y)));
            }
        }

        if(x.fullChildren().size() > 0){
            PQNode zf = null;
            if(x.fullChildren().size() == 1){
                // TODO: Let ZF be the unique element in FULL_CHILDREN(X)
                zf = x.fullChildren().get(0);
                // Remove zf from the CIRCULAR_LINK list of which it is currently a member
                zf.removeFromCircularLink();
            }
            else {
                zf = new PQNode();
                zf.nodeType = PQNode.PNODE;
                zf.labelType = PQNode.FULL;
                for(PQNode w : x.fullChildren()){
                    w.removeFromCircularLink();
                    w.parent = zf;
                }
                // Set the CIRCULAR_LINK fields of the nodes in FULL_CHILDREN(X) to form a doubly-linked circular list
                // - Possibly move this to PQNode class
                PQNode startN = x.fullChildren().get(0);
                PQNode endN = x.fullChildren().get(x.fullChildren().size()-1);
                startN.circularLink_prev = endN;
                startN.circularLink_next = x.fullChildren().get(1);
                endN.circularLink_next = startN;
                endN.circularLink_prev = x.fullChildren().get(x.fullChildren().size()-2);
                for(int i=1; i<x.fullChildren().size()-2; i++){
                    PQNode w = x.fullChildren().get(i);
                    w.circularLink_prev = x.fullChildren().get(i-1);
                    w.circularLink_next = x.fullChildren().get(i+1);
                }

                zf.childCount = x.fullChildren().size();
            }

            zf.parent = y;
            fc.setImmediateSiblings(union(fc.immediateSiblings(), Arrays.asList(zf)));
            zf.setImmediateSiblings(Arrays.asList(fc));
            y.endmostChildren().removeAll(union(Arrays.asList(fc), Arrays.asList(zf)));
        }

        int numberEmpty = x.childCount - x.fullChildren().size() - x.partialChildren().size();
        if(numberEmpty > 0){
            PQNode ze;
            if(numberEmpty == 1){
                ze = es; // Could be null (?)
            }
            else{
                ze = x;
                ze.labelType = PQNode.EMPTY;
                ze.childCount = numberEmpty;
            }
            ze.parent = y;
            ec.setImmediateSiblings(union(ec.immediateSiblings(), Arrays.asList(ze)));
            ze.setImmediateSiblings(Arrays.asList(ec));
            y.endmostChildren().removeAll(union(Arrays.asList(ec), Arrays.asList(ze)));
        }

        if(numberEmpty < 2){
            x = null;
        }
        return true;
    }
    public boolean TEMPLATE_P6(PQNode x){
        return false;
    }

    public boolean TEMPLATE_Q1(PQNode x){
       if (x.nodeType == PQNode.QNODE) {
           return GENERALIZED_TEMPLATE_1(x);
       }
       return false;
    }

    public boolean TEMPLATE_Q2(PQNode x){

        if(!x.nodeType(PQNode.QNODE)){
            return false;
        }
        if(x.nodeType(PQNode.PSEUDO_NODE)){
            return false;
        }

        if(x.fullChildren().size() > 0) {

            List<PQNode> xChildren = intersection(x.fullChildren(), x.endmostChildren());
            if (xChildren.size() != 1) {
                return false;
            }
            // let Y be the unique element in FULL_CHILDREN(X) union ENDMOST_CHILDREN(X)
            PQNode y = union(x.fullChildren(), x.endmostChildren()).get(0);

            for(int i=0; i<x.fullChildren().size(); i++){
                if(x.fullChildren().contains(y)){
                    return false;
                }
                else {
                    // Y := the next sibling in the chain of children of X
                    y = y.circularLink_next;
                }
            }

            if( !subset(x.partialChildren(), Arrays.asList(y)) ){
                return false;
            }

        }
        else if(!subset(x.partialChildren(), x.endmostChildren())){
            return false;
        }

        x.labelType = PQNode.PARTIAL;

        if(x.partialChildren().size() > 0){
            // TODO: Y := the unique element of PARTIAL_CHILDREN(X)
            PQNode y = x.partialChildren().get(0);

            PQNode fc = y.endmostChild(PQNode.FULL);

            PQNode yFullSibling = y.getImmediateSiblingOfNodeType(PQNode.FULL);
            if(yFullSibling != null){
                PQNode fs = yFullSibling;
                fs.immediateSiblings().removeAll(union(Arrays.asList(y), Arrays.asList(fc)));
                fc.setImmediateSiblings(union(fc.immediateSiblings(), Arrays.asList(fs)));
            }
            else {
                x.endmostChildren().removeAll(union(Arrays.asList(y), Arrays.asList(fc)));
                fc.parent = x;
            }

            PQNode ec =  y.endmostChild(PQNode.EMPTY);
            PQNode yEmptyImmSibling = y.getImmediateSiblingOfNodeType(PQNode.EMPTY);
            if(yEmptyImmSibling != null){
                PQNode es = yEmptyImmSibling;
                es.immediateSiblings().removeAll(Arrays.asList(y, ec));
                ec.setImmediateSiblings(union(ec.immediateSiblings(), Arrays.asList(es)));
            }
            else {
                x.endmostChildren().removeAll(Arrays.asList(y, ec));
                ec.parent = x;
            }
            y = null;
        }

        return true;
    }
    public boolean TEMPLATE_Q3(PQNode x){
        return false;
    }

}
















