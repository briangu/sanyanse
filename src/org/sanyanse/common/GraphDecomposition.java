package org.sanyanse.common;


import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.jblas.Eigen;
import org.jblas.FloatMatrix;


public class GraphDecomposition
{
  private FloatMatrix _adjacency;
  private FloatMatrix _degree;
  private volatile FloatMatrix _laplacian = null;
  private volatile FloatMatrix[] _adjSpectrum = null;
  private volatile FloatMatrix[] _lapSpectrum = null;

  private ReentrantReadWriteLock _rwlLaplacian = new ReentrantReadWriteLock();
  private ReentrantReadWriteLock _rwlAdjSpectrum = new ReentrantReadWriteLock();
  private ReentrantReadWriteLock _rwlLapSpectrum = new ReentrantReadWriteLock();

  private int _colWidth;

  private GraphDecomposition(FloatMatrix adj, FloatMatrix deg)
  {
    _adjacency = adj;
    _degree = deg;
    _colWidth = _adjacency.getColumns();
  }

  public int getColWidth()
  {
    return _colWidth;
  }

  public int getHeight()
  {
    return _colWidth;
  }

  public FloatMatrix getAdjacency()
  {
    return _adjacency;
  }

  public FloatMatrix getDegree()
  {
    return _degree;
  }

  public FloatMatrix getCentrality()
  {
    FloatMatrix[] e = getAdjacencySpectrum();
    FloatMatrix ev = e[0];
    return ev;
  }

  public FloatMatrix getLaplacian()
  {
    FloatMatrix result;

    _rwlLaplacian.readLock().lock();

    if (_laplacian == null)
    {
      _rwlLaplacian.readLock().unlock();
      _rwlLaplacian.writeLock().lock();

      if (_laplacian == null)
      {
        _laplacian = _degree.sub(_adjacency);
      }

      _rwlLaplacian.readLock().lock();
      _rwlLaplacian.writeLock().unlock();
    }

    result = _laplacian;

    _rwlLaplacian.readLock().unlock();

    return result;
  }

  public FloatMatrix[] getAdjacencySpectrum()
  {
    FloatMatrix[] result;

    _rwlAdjSpectrum.readLock().lock();

    if (_adjSpectrum == null)
    {
      _rwlAdjSpectrum.readLock().unlock();
      _rwlAdjSpectrum.writeLock().lock();

      if (_adjSpectrum == null)
      {
        _adjSpectrum = Eigen.symmetricEigenvectors(_adjacency);
      }

      _rwlAdjSpectrum.readLock().lock();
      _rwlAdjSpectrum.writeLock().unlock();
    }

    result = _adjSpectrum;

    _rwlAdjSpectrum.readLock().unlock();

    return result;
  }

  public FloatMatrix[] getLaplacianSpectrum()
  {
    FloatMatrix[] result;

    _rwlLapSpectrum.readLock().lock();

    if (_lapSpectrum == null)
    {
      _rwlLapSpectrum.readLock().unlock();
      _rwlLapSpectrum.writeLock().lock();

      if (_lapSpectrum == null)
      {
        _lapSpectrum = Eigen.symmetricEigenvectors(getLaplacian());
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
    FloatMatrix adjacency = new FloatMatrix(graph.NodeCount, graph.NodeCount);
//    FloatMatrix degree = new FloatMatrix(graph.NodeCount, graph.NodeCount);

    for (int i = 0; i < graph.NodeCount; i++)
    {
      ColorableNode node = graph.Nodes[i];

//      degree.put(i, i, info.EdgeSet.size());

      for (int neighborIdx : node.Edges)
      {
        adjacency.put(i, neighborIdx, 1.0f);
      }
    }

    return new GraphDecomposition(adjacency, null);
  }
}
