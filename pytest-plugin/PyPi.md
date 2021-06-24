
git clone https://github.com/Keysight/argus.git
cd ./argus/pytest-plugin/

python setup.py test

rm -rf build, dist, pytest_argus.egg-info

python setup.py bdist_wheel

pip uninstall pytest-argus -y

twine upload -u mirceadan -p <your pass> dist/*
