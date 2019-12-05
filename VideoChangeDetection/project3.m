% CMPEN-454 Project 3
% Team Members:
%   Eric Zhewen Li (zxl163)
%   Qingchun Zhang (qxz5094)
%   Xiaoxi Wang (xxw26)

% main function - takes in a series of frames and outputs processed frames
% Input - directory: folder name containing frames to be processed
% Input - threshold: threshold value to generate binary image
%                    default value is 0.08
% Input - absAlpha: alpha value for adaptive background subtraction
%                   default value is 0.5
% Input - pfdGamma: gamma value for persistent frame differencing
%                   default value is 10
% Output - processed images are stored in a new directory
%          named "processed-images", with frame names start with 
%          original direcoty name and followed by original directory name
%          e.g. "getin-f0002.jpg"
%          in the directory, files with same name will be overwrriten
% Note - Assuming all frames from provided directory are of same size

function project3(directory,threshold,absAlpha,pfdGamma)
    
    % creating default values
    if ~exist('threshold','var')
        % use default threshold if none provided
        threshold = 0.08;
    end
    if ~exist('absAlpha','var')
        absAlpha = 0.5;
    end
    if ~exist('pfdGamma','var')
        pfdGamma = 10;
    end
    
    % create folder to save the output images
    outputDirectory = 'processed-images';
    mkdir(outputDirectory);
    folderInfo = dir(directory);
    % ignoring '.' and '..' files
    backgroundLoc = sprintf('%s/%s',directory,folderInfo(3).name);
    
    % get first frame as background for subtraction
    background = imread(backgroundLoc);
    % taking the green channel to convert to greyscale
    background = background(:, :, 2);
    % for adaptive frame diff - B(0)=I(0)
    prevB = background;
    
    xsize = length(background(:,1));
    ysize = length(background(1,:));
    % For persistent frame diff - H(0) = 0
    prevH = zeros(xsize, ysize, 'double');
    
    for i = 4:length(folderInfo) % iterate thru folder
        % read in frames as images
        prevFrame = imread(sprintf('%s/%s',directory,folderInfo(i-1).name));
        frame = imread(sprintf('%s/%s',directory,folderInfo(i).name));
        % taking the green channel to convert to greyscale
        prevFrame = prevFrame(:, :, 2);
        frame = frame(:, :, 2);
        
        % ---background sub algorithm---
        bgSubNewFrame = abs(frame - background);        
        
        % ---frame differencing algorithm---
        frameDiffNewFrame = abs(frame - prevFrame);
        
        % ---adaptive background sub algorithm---
        adapBgSubNewFrame = abs(frame - prevB);
        % update B(t-1) for next iteration
        prevB = absAlpha.*frame + (1-absAlpha).*prevB;
        
        % convert results to binary images
        bgSubNewFrameBin = im2bw(bgSubNewFrame,threshold);
        frameDiffNewFrameBin = im2bw(frameDiffNewFrame,threshold);
        adapBgSubNewFrameBin = im2bw(adapBgSubNewFrame,threshold);
        
        % ---persistent frame differencing algorithm---
        tmp = max(prevH - pfdGamma,0);
        persFrameDiffNewFrame = max(255.*frameDiffNewFrameBin,tmp);
        % update prevH for next iteration
        prevH = persFrameDiffNewFrame;
        % normalize the result
        persFrameDiffNewFrame = double(persFrameDiffNewFrame)./ ...
            double(max(persFrameDiffNewFrame(:)));
        
        % output processed images to a new directory
        imwrite([bgSubNewFrameBin frameDiffNewFrameBin; ...
                adapBgSubNewFrameBin persFrameDiffNewFrame], ...
                sprintf('%s/%s-%s',outputDirectory,directory,folderInfo(i).name));
        
    end
    
end