package org.sanyanse.common;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;


public class GraphDecomposition
{
  private Matrix _adjacency;
  private Matrix _degree;
  private volatile Matrix _laplacian = null;
  private volatile EigenvalueDecomposition _adjSpectrum = null;
  private volatile EigenvalueDecomposition _lapSpectrum = null;

  private ReentrantReadWriteLock _rwlLaplacian = new ReentrantReadWriteLock();
  private ReentrantReadWriteLock _rwlAdjSpectrum = new ReentrantReadWriteLock();
  private ReentrantReadWriteLock _rwlLapSpectrum = new ReentrantReadWriteLock();

  private int _length;

  public GraphDecomposition(Matrix adj, Matrix deg)
  {
    _adjacency = adj;
    _degree = deg;
    _length = _adjacency.getColumnDimension();
  }

  public int getLength()
  {
    return _length;
  }

  public int getHeight()
  {
    return _length;
  }

  public Matrix getAdjacency()
  {
    return _adjacency;
  }

  public Matrix getDegree()
  {
    return _degree;
  }

  public Map<String, Float> getCentrality(Graph graph)
  {
    Map<String, Float> metric = new HashMap<String, Float>(graph.NodeCount);
    EigenvalueDecomposition e = getAdjacencySpectrum();
    Matrix eigenvectors = e.getV();
    int j = _length-1;

    for (int i = 0; i < _length; i++)
    {
      metric.put(graph.Nodes[i].Id, (float)eigenvectors.get(i, j));
    }

    return metric;
  }

  public Matrix getLaplacian()
  {
    Matrix result;

    _rwlLaplacian.readLock().lock();

    if (_laplacian == null)
    {
      _rwlLaplacian.readLock().unlock();
      _rwlLaplacian.writeLock().lock();

      if (_laplacian == null)
      {
        _laplacian = _degree.minus(_adjacency);
      }

      _rwlLaplacian.readLock().lock();
      _rwlLaplacian.writeLock().unlock();
    }

    result = _laplacian;

    _rwlLaplacian.readLock().unlock();

    return result;
  }

  public EigenvalueDecomposition getAdjacencySpectrum()
  {
    EigenvalueDecomposition result;

    _rwlAdjSpectrum.readLock().lock();

    if (_adjSpectrum == null)
    {
      _rwlAdjSpectrum.readLock().unlock();
      _rwlAdjSpectrum.writeLock().lock();

      if (_adjSpectrum == null)
      {
        _adjSpectrum = _adjacency.eig();
      }

      _rwlAdjSpectrum.readLock().lock();
      _rwlAdjSpectrum.writeLock().unlock();
    }

    result = _adjSpectrum;

    _rwlAdjSpectrum.readLock().unlock();

    return result;
  }

  public EigenvalueDecomposition getLaplacianSpectrum()
  {
    EigenvalueDecomposition result;

    _rwlLapSpectrum.readLock().lock();

    if (_lapSpectrum == null)
    {
      _rwlLapSpectrum.readLock().unlock();
      _rwlLapSpectrum.writeLock().lock();

      if (_lapSpectrum == null)
      {
        _lapSpectrum = getLaplacian().eig();
      }

      _rwlLapSpectrum.readLock().lock();
      _rwlLapSpectrum.writeLock().unlock();
    }

    result = _lapSpectrum;

    _rwlLapSpectrum.readLock().unlock();

    return result;
  }
}
