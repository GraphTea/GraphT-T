package graphtea.extensions.reports.planarity.planaritypq;

import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static graphtea.extensions.reports.planarity.planaritypq.PQHelpers.setCircularLinks;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertTrue;


public class PQTest {

    @Test
    public void createPQNode(){
        PQNode node = new PQNode();
        assertNotNull(node);
    }

    /** Tests bubbling up
     * Checks:
     * - Returns null */
    @Test
    public void bubbleSEmptyTest(){
        PQNode _root = new PQNode();
        PQNode A = new PQNode();
        PQNode B = new PQNode();
        PQNode C = new PQNode();

        // Create tree
        _root.nodeType = PQNode.PNODE;
        A.nodeType = PQNode.PSEUDO_NODE;
        B.nodeType = PQNode.PSEUDO_NODE;
        C.nodeType = PQNode.PSEUDO_NODE;

        _root.children = Arrays.asList(A, B, C);
        A.parent = _root;
        B.parent = _root;
        C.parent = _root;

        // Constraint set S := {B,C}
        List<PQNode> S = new ArrayList<>();

        // Test PQTree
        PQ PQTree = new PQ();
        PQNode tree = PQTree.bubble(_root, S);

        // todo: Check if the output tree is the same as the input tree
        assertTrue(tree == _root);
    }

    /** Tests bubbling up
     * Checks:
     * - All nodes processed
     * - Marks all nodes in PRUNED(T,S)*/
    @Test
    public void bubbleTest1_PNode(){

        PQNode _root = new PQNode();
        PQNode A = new PQNode();
        PQNode B = new PQNode();
        PQNode C = new PQNode();

        // Create tree
        _root.nodeType = PQNode.PNODE;
        A.nodeType = PQNode.PSEUDO_NODE;
        B.nodeType = PQNode.PSEUDO_NODE;
        C.nodeType = PQNode.PSEUDO_NODE;

        _root.children = Arrays.asList(A, B, C);
        A.parent = _root;
        B.parent = _root;
        C.parent = _root;
        List<PQNode> list = new ArrayList<>(Arrays.asList(A,B,C));
        setCircularLinks(list);

        // Constraint set S := {B,C}
        List<PQNode> S = Arrays.asList(B, C);

        // Test PQTree
        PQ PQTree = new PQ();
        PQNode r = PQTree.bubble(_root, S);

        // ASSERT
        assertTrue(r.children.contains(A));
        assertTrue(r.children.contains(B));
        assertTrue(r.children.contains(C));
        assertTrue(r.nodeType(PQNode.PNODE));
        assertTrue(!B.blocked);
        assertTrue(!C.blocked);
    }

    @Test
    public void bubbleTest2_QNodePNode(){
        PQNode _root = new PQNode();
        PQNode A = new PQNode();
        PQNode B = new PQNode();
        PQNode C = new PQNode();
        PQNode qNode = new PQNode();
        PQNode D = new PQNode();
        PQNode E = new PQNode();
        PQNode F = new PQNode();


        // Create tree
        _root.nodeType = PQNode.PNODE;
        A.nodeType = PQNode.PSEUDO_NODE;
        B.nodeType = PQNode.PSEUDO_NODE;
        C.nodeType = PQNode.PSEUDO_NODE;
        qNode.nodeType = PQNode.QNODE;
        D.nodeType = PQNode.PSEUDO_NODE;
        E.nodeType = PQNode.PSEUDO_NODE;
        F.nodeType = PQNode.PSEUDO_NODE;

        _root.children = Arrays.asList(A, B, C, qNode);
        A.parent = _root;
        B.parent = _root;
        C.parent = _root;
        qNode.parent = _root;
        D.parent = qNode;
        //E.parent = qNode;
        F.parent = qNode;

        setCircularLinks(Arrays.asList(qNode,A,B,C));
        setCircularLinks(Arrays.asList(D,E,F));

        // Constraint set S
        List<PQNode> S = Arrays.asList(D, E, F);

        // Test PQTree
        PQ PQTree = new PQ();
        PQNode r = PQTree.bubble(_root, S);

        // ASSERT
        assertTrue(D.parent == qNode);
        assertTrue(E.parent == qNode);
        assertTrue(F.parent == qNode);
        assertTrue(!D.blocked);
        assertTrue(!E.blocked);
        assertTrue(!F.blocked);

    }

    @Test
    public void bubbleTest3_QNodePNode(){
        PQNode _root = new PQNode();
        PQNode A = new PQNode();
        PQNode B = new PQNode();
        PQNode C = new PQNode();
        PQNode qNode = new PQNode();
        PQNode D = new PQNode();
        PQNode E = new PQNode();
        PQNode F = new PQNode();

        // Create tree
        _root.nodeType = PQNode.PNODE;
        A.nodeType = PQNode.PSEUDO_NODE;
        B.nodeType = PQNode.PSEUDO_NODE;
        C.nodeType = PQNode.PSEUDO_NODE;
        qNode.nodeType = PQNode.QNODE;
        D.nodeType = PQNode.PSEUDO_NODE;
        E.nodeType = PQNode.PSEUDO_NODE;
        F.nodeType = PQNode.PSEUDO_NODE;

        _root.children = Arrays.asList(A, B, C, qNode);
        A.parent = _root;
        B.parent = _root;
        C.parent = _root;
        qNode.parent = _root;
        D.parent = qNode;
        //E.parent = qNode;
        F.parent = qNode;

        setCircularLinks(Arrays.asList(qNode,A,B,C));
        setCircularLinks(Arrays.asList(D,E,F));

        // Constraint set S
        List<PQNode> S = Arrays.asList(D, E, A);

        // Test PQTree
        PQ PQTree = new PQ();
        PQNode r = PQTree.bubble(_root, S);

        // ASSERT
        assertTrue(A.parent == r);
        assertTrue(D.parent == qNode);
        assertTrue(E.parent == qNode);
        assertTrue(!D.blocked);
        assertTrue(!E.blocked);
        assertTrue(!A.blocked);
    }

    @Test
    public void bubbleTest4_QNodePNode(){
        PQNode _root = new PQNode();
        PQNode A = new PQNode();
        PQNode B = new PQNode();
        PQNode C = new PQNode();
        PQNode qNode = new PQNode();
        PQNode D = new PQNode();
        PQNode E = new PQNode();
        PQNode F = new PQNode();
        PQNode G = new PQNode();
        PQNode H = new PQNode();

        // Create tree
        _root.nodeType = PQNode.PNODE;
        A.nodeType = PQNode.PSEUDO_NODE;
        B.nodeType = PQNode.PSEUDO_NODE;
        C.nodeType = PQNode.PSEUDO_NODE;
        qNode.nodeType = PQNode.QNODE;
        D.nodeType = PQNode.PSEUDO_NODE;
        E.nodeType = PQNode.PSEUDO_NODE;
        F.nodeType = PQNode.PSEUDO_NODE;
        G.nodeType = PQNode.PSEUDO_NODE;
        H.nodeType = PQNode.PSEUDO_NODE;

        _root.id = "root";
        A.id = "A";
        B.id = "B";
        C.id = "C";
        D.id = "D";
        E.id = "E";
        F.id = "F";
        G.id = "G";
        H.id = "H";


        _root.children = Arrays.asList(A, B, C, qNode);
        A.parent = _root;
        B.parent = _root;
        C.parent = _root;
        qNode.parent = _root;
        D.parent = qNode;
        E.parent = qNode;
        F.parent = qNode;
        G.parent = qNode;
        H.parent = qNode;


        setCircularLinks(Arrays.asList(qNode,A,B,C));
        setCircularLinks(Arrays.asList(D,E,F,G,H));

        // Constraint set S
        List<PQNode> S = Arrays.asList(F, B, C);

        // Test PQTree
        PQ PQTree = new PQ();
        PQNode r = PQTree.bubble(_root, S);

        // ASSERT
        assertTrue(F.parent == qNode);
        assertTrue(B.parent == r);
        assertTrue(C.parent == r);
        assertTrue(!F.blocked);
        assertTrue(!B.blocked);
        assertTrue(!C.blocked);

    }

    @Test
    public void reduceTest(){
        PQNode _root = new PQNode();
        PQNode A = new PQNode();
        PQNode B = new PQNode();
        PQNode C = new PQNode();

        // Create tree
        _root.nodeType = PQNode.PNODE;
        A.nodeType = PQNode.PSEUDO_NODE;
        B.nodeType = PQNode.PSEUDO_NODE;
        C.nodeType = PQNode.PSEUDO_NODE;

        _root.children = Arrays.asList(A, B, C);
        A.parent = _root;
        B.parent = _root;
        C.parent = _root;
        List<PQNode> list = new ArrayList<>(Arrays.asList(A,B,C));
        setCircularLinks(list);

        // Constraint set S := {B,C}
        List<PQNode> S = Arrays.asList(B, C);

        PQ PQTree = new PQ();
        PQNode r = PQTree.reduce(_root, S);

        assertTrue(r != null);

    }

    @Test
    public void templateP1Test01(){
        PQNode _root = new PQNode();
        PQNode A = new PQNode();
        PQNode B = new PQNode();

        _root.children = Arrays.asList(A,B);
        A.parent = _root;
        B.parent = _root;
        _root.labelType = PQNode.EMPTY;
        A.labelType = PQNode.FULL;
        B.labelType = PQNode.FULL;

        PQ PQTree = new PQ();

        // All children blocked
        assertTrue(!_root.labelType.equals(PQNode.FULL));
        boolean rt = PQTree.TEMPLATE_P1(_root);
        assertTrue(rt);
        assertTrue(_root.labelType.equals(PQNode.FULL));

        // One child not blocked
        PQNode C = new PQNode();
        C.parent = _root;
        _root.labelType = PQNode.EMPTY;
        C.labelType = PQNode.EMPTY;

        _root.children = Arrays.asList(A,B,C);
        rt = PQTree.TEMPLATE_P1(_root);
        assertTrue(!rt);
        assertTrue(!_root.labelType.equals(PQNode.FULL));
    }

    @Test
    public void templateP2Test(){
        PQNode _root = new PQNode();
        PQNode A = new PQNode();
        PQNode B = new PQNode();
        PQNode C = new PQNode();
        PQNode D = new PQNode();
        PQNode E = new PQNode();

        A.parent = _root;
        B.parent = _root;
        C.parent = _root;
        D.parent = _root;
        E.parent = _root;

        A.labelType = PQNode.FULL;
        B.labelType = PQNode.FULL;
        C.labelType = PQNode.FULL;

        D.labelType = PQNode.EMPTY;
        E.labelType = PQNode.EMPTY;

        List<PQNode> list = new ArrayList<>(Arrays.asList(A,B,C,D,E));
        _root.children = list;
        setCircularLinks(list);

        PQ PQTree = new PQ();
        boolean rt = PQTree.TEMPLATE_P2(_root);

        assertTrue(A.parent != _root);
        assertTrue(B.parent != _root);
        assertTrue(C.parent != _root);

        assertTrue(D.parent == _root);
        assertTrue(E.parent == _root);

        assertTrue(A.parent.nodeType.equals(PQNode.PNODE));
        assertTrue(B.parent.nodeType.equals(PQNode.PNODE));
        assertTrue(C.parent.nodeType.equals(PQNode.PNODE));

        assertTrue(A.parent.labelType.equals(PQNode.FULL));

    }

    @Test
    public void templateP3Test(){
        PQNode _root = new PQNode();
        PQNode A = new PQNode();
        PQNode B = new PQNode();
        PQNode C = new PQNode();
        PQNode D = new PQNode();
        PQNode E = new PQNode();

        A.parent = _root;
        B.parent = _root;
        C.parent = _root;
        D.parent = _root;
        E.parent = _root;

        A.labelType = PQNode.FULL;
        B.labelType = PQNode.FULL;
        C.labelType = PQNode.FULL;

        D.labelType = PQNode.EMPTY;
        E.labelType = PQNode.EMPTY;

        List<PQNode> list = new ArrayList<>(Arrays.asList(A,B,C,D,E));
        _root.children = list;
        setCircularLinks(list);

        PQ PQTree = new PQ();
        boolean rt = PQTree.TEMPLATE_P3(_root);
        assertTrue(_root.nodeType == PQNode.QNODE);
        assertTrue(_root.labelType == PQNode.PARTIAL);
        assertTrue(_root.children.size() == 2);
        for (PQNode notFullChild : _root.children.get(0).children) {
            assertTrue(notFullChild.labelType != PQNode.FULL);
        }
        for (PQNode fullChild : _root.children.get(1).children) {
            assertTrue(fullChild.labelType == PQNode.FULL);
        }
    }

    @Test
    public void templateP4Test(){
        PQNode _root = new PQNode();
        PQNode A = new PQNode();
        PQNode B = new PQNode();
        PQNode C = new PQNode();
        PQNode D = new PQNode();

        PQNode qNode = new PQNode();
        PQNode E = new PQNode();
        PQNode F = new PQNode();
        PQNode G = new PQNode();
        PQNode H = new PQNode();

        A.parent = _root;
        B.parent = _root;
        C.parent = _root;
        D.parent = _root;

        qNode.parent = _root;
        E.parent = qNode;
        F.parent = qNode;
        G.parent = qNode;
        H.parent = qNode;

        A.labelType = PQNode.EMPTY;
        B.labelType = PQNode.EMPTY;
        C.labelType = PQNode.FULL;
        D.labelType = PQNode.FULL;

        qNode.labelType = PQNode.PARTIAL;
        E.labelType = PQNode.EMPTY;
        F.labelType = PQNode.EMPTY;
        G.labelType = PQNode.FULL;
        H.labelType = PQNode.FULL;

        _root.labelType = PQNode.PARTIAL;

        qNode.nodeType = PQNode.QNODE;
        _root.nodeType = PQNode.PNODE;

        List<PQNode> rootChildren = new ArrayList<>(Arrays.asList(A,B,qNode,C,D));
        List<PQNode> qNodeChildren = new ArrayList<>(Arrays.asList(E,F,G,H));
        setCircularLinks(rootChildren);
        setCircularLinks(qNodeChildren);
        _root.children = rootChildren;
        qNode.children = qNodeChildren;

        PQ PQTree = new PQ();
        boolean rt = PQTree.TEMPLATE_P4(_root);

        assertTrue(rt);
        assertTrue(A.parent == _root);
        assertTrue(B.parent == _root);
        assertTrue(qNode.parent == _root);

        assertTrue(C.parent != qNode && C.parent != _root);
        assertTrue(D.parent != qNode && D.parent != _root);

        assertTrue(E.parent == qNode);
        assertTrue(F.parent == qNode);
        assertTrue(G.parent == qNode);
        assertTrue(H.parent == qNode);
        assertTrue(C.parent == D.parent);
        assertTrue(C.parent.parent == qNode);
        assertTrue(qNode.children.contains(C.parent));
        assertTrue(_root.children.contains(qNode));
        assertTrue(C.parent.nodeType(PQNode.PNODE));
        assertTrue(C.parent.labelType.equals(PQNode.FULL));
        assertTrue(qNode.labelType.equals(PQNode.PARTIAL));

    }

    @Test
    public void templateP5Test(){

    }

    @Test
    public void templateP6Test(){

    }

    @Test
    public void templateQ1Test(){

    }

    @Test
    public void templateQ2Test(){
        PQNode _root = new PQNode();

        PQNode qNode = new PQNode();
        PQNode A = new PQNode();
        PQNode B = new PQNode();
        PQNode C = new PQNode();
        PQNode Ca = new PQNode();
        PQNode Cb = new PQNode();
        PQNode Cc = new PQNode();
        PQNode Cd = new PQNode();
        PQNode D = new PQNode();
        PQNode E = new PQNode();

        _root.nodeType = PQNode.PNODE;
        _root.children = Arrays.asList(qNode);

        qNode.nodeType = PQNode.QNODE;
        C.nodeType = PQNode.QNODE;

        qNode.parent = _root;
        A.parent = qNode;
        B.parent = qNode;
        C.parent = qNode;
        Ca.parent = C;
        Cb.parent = C;
        Cc.parent = C;
        Cd.parent = C;
        D.parent = qNode;
        E.parent = qNode;

        A.id = "A";
        B.id = "B";
        C.id = "C";
        D.id = "D";
        E.id = "E";
        Ca.id = "Ca";
        Cb.id = "Cb";
        Cc.id = "Cc";
        Cd.id = "Cd";
        qNode.id = "qNode";
        _root.id = "_root";

        qNode.labelType = PQNode.PARTIAL;
        A.labelType = PQNode.EMPTY;
        B.labelType = PQNode.EMPTY;
        C.labelType = PQNode.PARTIAL;
        Ca.labelType = PQNode.EMPTY;
        Cb.labelType = PQNode.EMPTY;
        Cc.labelType = PQNode.FULL;
        Cd.labelType = PQNode.FULL;
        D.labelType = PQNode.FULL;
        E.labelType = PQNode.FULL;

        qNode.children = Arrays.asList(A, B, C, D, E);
        setCircularLinks(Arrays.asList(A, B, C, D, E));
        setCircularLinks(Arrays.asList(Ca, Cb, Cc, Cd));

        PQ PQTree = new PQ();
        boolean rt = PQTree.TEMPLATE_Q2(qNode);

        assertTrue(rt);

        /** Travels from left to right in the children list and counts
         * the number of children that switch from empty to non-empty
         * or vice versa, we call this number the 'number of flips'.
         *
         * This number has to be 1 to be valid. */
        int flips = 0;
        String lastState = PQNode.EMPTY;
        for (PQNode child : qNode.children) {
            System.out.println(child.labelType);
            if (lastState != child.labelType) {
                flips++;
            }
        }
        System.out.println(flips);
        assertTrue(flips == 1);
    }

    @Test
    public void templateL1Test(){

    }

}

