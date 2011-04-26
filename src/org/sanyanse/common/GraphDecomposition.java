package org.sanyanse.common;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.ejml.alg.dense.decomposition.eig.EigenPowerMethod;
import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;


public class GraphDecomposition
{
  private DenseMatrix64F _adjacency;
  private DenseMatrix64F _degree;
  private volatile DenseMatrix64F _laplacian = null;
  private volatile DenseMatrix64F _adjSpectrum = null;
  private volatile DenseMatrix64F _lapSpectrum = null;

  private ReentrantReadWriteLock _rwlLaplacian = new ReentrantReadWriteLock();
  private ReentrantReadWriteLock _rwlAdjSpectrum = new ReentrantReadWriteLock();
  private ReentrantReadWriteLock _rwlLapSpectrum = new ReentrantReadWriteLock();

  private int _length;

  public GraphDecomposition(DenseMatrix64F adj, DenseMatrix64F deg)
  {
    _adjacency = adj;
    _degree = deg;
    _length = _adjacency.numCols;
  }

  public int getLength()
  {
    return _length;
  }

  public int getHeight()
  {
    return _length;
  }

  public DenseMatrix64F getAdjacency()
  {
    return _adjacency;
  }

  public DenseMatrix64F getDegree()
  {
    return _degree;
  }

  public Map<String, Float> getCentrality(Graph graph)
  {
    Map<String, Float> metric = new HashMap<String, Float>(graph.NodeCount);
    DenseMatrix64F e = getAdjacencySpectrum();
    double[] f = e.data;
    int j = _length-1;

    for (int i = 0; i < _length; i++)
    {
      metric.put(graph.Nodes[i].Id, (float) f[i]);
    }

    return metric;
  }

  public DenseMatrix64F getLaplacian()
  {
    DenseMatrix64F result;

    _rwlLaplacian.readLock().lock();

    if (_laplacian == null)
    {
      _rwlLaplacian.readLock().unlock();
      _rwlLaplacian.writeLock().lock();

      if (_laplacian == null)
      {
        _laplacian = new DenseMatrix64F(_length*_length);
        CommonOps.sub(_degree, _adjacency, _laplacian);
      }

      _rwlLaplacian.readLock().lock();
      _rwlLaplacian.writeLock().unlock();
    }

    result = _laplacian;

    _rwlLaplacian.readLock().unlock();

    return result;
  }

  public DenseMatrix64F getAdjacencySpectrum()
  {
    DenseMatrix64F result;

    _rwlAdjSpectrum.readLock().lock();

    if (_adjSpectrum == null)
    {
      _rwlAdjSpectrum.readLock().unlock();
      _rwlAdjSpectrum.writeLock().lock();

      if (_adjSpectrum == null)
      {
//        Eigenpair pair = EigenOps.dominantEigenpair(_adjacency);
//        _adjSpectrum = pair.vector;
        EigenPowerMethod epm = new EigenPowerMethod(_length);
        epm.setOptions(10, 0.5);
        boolean converged = epm.computeShiftInvert(_adjacency, 0.5);
        if (!converged)
        {
          System.out.println("failed to converge");
        }
        _adjSpectrum = epm.getEigenVector();
      }

      _rwlAdjSpectrum.readLock().lock();
      _rwlAdjSpectrum.writeLock().unlock();
    }

    result = _adjSpectrum;

    _rwlAdjSpectrum.readLock().unlock();

    return result;
  }

  public DenseMatrix64F getLaplacianSpectrum()
  {
    DenseMatrix64F result;

    _rwlLapSpectrum.readLock().lock();

    if (_lapSpectrum == null)
    {
      _rwlLapSpectrum.readLock().unlock();
      _rwlLapSpectrum.writeLock().lock();

      if (_lapSpectrum == null)
      {
        EigenPowerMethod epm = new EigenPowerMethod(_length*_length);
        epm.computeDirect(getLaplacian());
        _lapSpectrum = epm.getEigenVector();
      }

      _rwlLapSpectrum.readLock().lock();
      _rwlLapSpectrum.writeLock().unlock();
    }

    result = _lapSpectrum;

    _rwlLapSpectrum.readLock().unlock();

    return result;
  }

  public static GraphDecomposition createFrom(Graph graph)
  {
    DenseMatrix64F adjacency = new DenseMatrix64F(graph.NodeCount, graph.NodeCount);
    DenseMatrix64F degree = new DenseMatrix64F(graph.NodeCount, graph.NodeCount);

    Map<String, GraphNodeInfo> nodeMap = graph.NodeMap;

    for (int i = 0; i < graph.NodeCount; i++)
    {
      ColorableNode node = graph.Nodes[i];
      GraphNodeInfo info = graph.NodeMap.get(node.Id);

      degree.set(i, i, info.EdgeSet.size());

      for (ColorableNode neighbor : info.EdgeSet)
      {
        GraphNodeInfo neighborInfo = nodeMap.get(neighbor.Id);
        adjacency.set(i, info.Index, info.EdgeSet.contains(neighborInfo.Node) ? 1.0 : 0.0);
      }
    }

    return new GraphDecomposition(adjacency, degree);
  }
}
